package com.hawk.sdufeforumpro.article.domain.service;

import cn.hutool.core.lang.Assert;
import com.alibaba.fastjson.JSON;
import com.alicp.jetcache.Cache;
import com.alicp.jetcache.CacheManager;
import com.alicp.jetcache.anno.CacheRefresh;
import com.alicp.jetcache.anno.CacheType;
import com.alicp.jetcache.anno.Cached;
import com.alicp.jetcache.template.QuickConfig;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hawk.sdufeforumpro.api.article.constant.ArticleConstant;
import com.hawk.sdufeforumpro.api.article.constant.ArticleOperateType;
import com.hawk.sdufeforumpro.api.article.request.ArticleAddRequest;
import com.hawk.sdufeforumpro.api.article.request.ArticlePageQueryRequest;
import com.hawk.sdufeforumpro.api.article.response.ArticleOperatorResponse;
import com.hawk.sdufeforumpro.article.domain.entity.Article;
import com.hawk.sdufeforumpro.article.domain.entity.ArticleEs;
import com.hawk.sdufeforumpro.article.domain.exception.ArticleErrorCode;
import com.hawk.sdufeforumpro.article.domain.exception.ArticleException;
import com.hawk.sdufeforumpro.article.infrastructure.ArticleMapper;
import com.hawk.sdufeforumpro.base.exception.BizException;
import com.hawk.sdufeforumpro.base.exception.RepoErrorCode;
import com.hawk.sdufeforumpro.base.response.PageResponse;
import com.hawk.sdufeforumpro.limiter.SlidingWindowRateLimiter;
import com.hawk.sdufeforumpro.lock.DistributeLock;
import com.hawk.sdufeforumpro.mq.param.eventMassage.EntityType;
import com.hawk.sdufeforumpro.mq.param.eventMassage.EventModel;
import com.hawk.sdufeforumpro.mq.param.eventMassage.EventType;
import com.hawk.sdufeforumpro.mq.producer.StreamProducer;
import org.apache.commons.lang3.StringUtils;
import org.redisson.api.RScoredSortedSet;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.Criteria;
import org.springframework.data.elasticsearch.core.query.CriteriaQuery;
import org.springframework.data.elasticsearch.core.query.Query;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.time.Duration;
import java.util.Date;
import java.util.concurrent.TimeUnit;

@Service
public class ArticleService extends ServiceImpl<ArticleMapper, Article> {

    @Autowired
    private ArticleOperateStreamService articleOperateStreamService;

    @Autowired
    private ElasticsearchOperations elasticsearchOperations;

    @Autowired
    private RedissonClient redissonClient;

    @Autowired
    private SlidingWindowRateLimiter slidingWindowRateLimiter;

    @Autowired
    private StreamProducer streamProducer;

    @Autowired
    private CacheManager cacheManager;

    private Cache<String, Article> idArticleCache;

    @PostConstruct
    public void init() {
        QuickConfig idQc = QuickConfig.newBuilder(":article:cache:id:")
                .cacheType(CacheType.BOTH)
                .expire(Duration.ofHours(2))
                .syncLocal(true)
                .build();
        idArticleCache = cacheManager.getOrCreateCache(idQc);
    }

    @DistributeLock(keyExpression = "#articleAddRequest.userId", scene = "USER_REGISTER")
    public ArticleOperatorResponse addArticle(ArticleAddRequest articleAddRequest) {
        Article article = new Article();
        article.addArticle(articleAddRequest.getContent(), Long.valueOf(articleAddRequest.getUserId()));
        Assert.isTrue(save(article), () -> new ArticleException(ArticleErrorCode.ARTICLE_OPERATE_FAILED));
        updateArticleCache(String.valueOf(article.getId()), article);

        long streamResult = articleOperateStreamService.insertStream(article, ArticleOperateType.CREATE);
        Assert.notNull(streamResult, () -> new BizException(RepoErrorCode.UPDATE_FAILED));

        ArticleOperatorResponse articleOperatorResponse = new ArticleOperatorResponse();
        articleOperatorResponse.setSuccess(true);
        return articleOperatorResponse;
    }

    @Cached(name = ":article:cache:id", cacheType = CacheType.BOTH, key = "#articleId", cacheNullValue = true)
    @CacheRefresh(refresh = 60, timeUnit = TimeUnit.MINUTES)
    public Article findById(Long articleId) {
        QueryWrapper<Article> wrapper = new QueryWrapper<>();
        wrapper.eq("id", articleId);
        return getOne(wrapper);
    }

    public PageResponse<ArticleEs> pageQueryByContent(ArticlePageQueryRequest articlePageQueryRequest) {
        Criteria criteria = null;
        if (StringUtils.isNotBlank(articlePageQueryRequest.getContent())) {
            criteria = new Criteria("content").matches(articlePageQueryRequest.getContent()).and(new Criteria("deleted").is("0"));
        } else {
            criteria = new Criteria("deleted").is("0");
        }

        PageRequest pageRequest = PageRequest.of(articlePageQueryRequest.getCurrentPage() - 1, articlePageQueryRequest.getPageSize());
        Query query = new CriteriaQuery(criteria).setPageable(pageRequest).addSort(Sort.by(Sort.Order.asc("create_time")));
        SearchHits<ArticleEs> searchHits = elasticsearchOperations.search(query, ArticleEs.class);

        return PageResponse.of(searchHits.getSearchHits().stream().map(SearchHit::getContent).toList(), (int) searchHits.getTotalHits(), articlePageQueryRequest.getCurrentPage(), articlePageQueryRequest.getPageSize());
    }

    public void like(String articleId, String userId) {
        Boolean access = slidingWindowRateLimiter.tryAcquire(articleId + ":" + userId, 1, 1);

        if (!access) {
            throw new ArticleException(ArticleErrorCode.LIKE_TOO_FREQUENT);
        }

        RScoredSortedSet<String> likeSet = redissonClient.getScoredSortedSet(ArticleConstant.LIKE_KEY_PREFIX + articleId);
        likeSet.add((double) new Date().getTime() / 1000, userId);

        EventModel eventModel = new EventModel();
        eventModel.setEventType(EventType.LIKE);
        eventModel.setActorId(Long.valueOf(userId));
        eventModel.setEntityType(EntityType.ARTICLE);
        eventModel.setEntityId(Long.valueOf(articleId));
        eventModel.setEntityOwnerId(findById(Long.valueOf(articleId)).getUserId());

        streamProducer.send("like-out-0", eventModel.getEventType().name(), JSON.toJSONString(eventModel));
    }

    public Long getLikeCount(String articleId) {
        RScoredSortedSet<String> likeSet = redissonClient.getScoredSortedSet(ArticleConstant.LIKE_KEY_PREFIX + articleId);
        return likeSet.stream().count();
    }

    private void updateArticleCache(String articleId, Article article) {
        idArticleCache.put(articleId, article);
    }
}
