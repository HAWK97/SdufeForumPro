package com.hawk.sdufeforumpro.article.domain.service;

import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hawk.sdufeforumpro.api.article.constant.ArticleOperateType;
import com.hawk.sdufeforumpro.article.domain.entity.Article;
import com.hawk.sdufeforumpro.article.domain.entity.ArticleOperateStream;
import com.hawk.sdufeforumpro.article.infrastructure.ArticleOperateStreamMapper;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class ArticleOperateStreamService extends ServiceImpl<ArticleOperateStreamMapper, ArticleOperateStream> {

    public Long insertStream(Article article, ArticleOperateType type) {
        ArticleOperateStream stream = new ArticleOperateStream();
        stream.setArticleId(String.valueOf(article.getId()));
        stream.setUserId(String.valueOf(article.getUserId()));
        stream.setOperateTime(new Date());
        stream.setType(type.name());
        stream.setParam(JSON.toJSONString(article));
        boolean result = save(stream);
        if (result) {
            return stream.getId();
        }
        return null;
    }
}
