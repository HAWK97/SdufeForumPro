package com.hawk.sdufeforumpro.article.controller;

import cn.dev33.satoken.stp.StpUtil;
import com.hawk.sdufeforumforumpro.web.vo.MultiResult;
import com.hawk.sdufeforumforumpro.web.vo.Result;
import com.hawk.sdufeforumpro.api.article.request.ArticleAddRequest;
import com.hawk.sdufeforumpro.api.article.request.ArticlePageQueryRequest;
import com.hawk.sdufeforumpro.api.article.response.data.ArticleInfo;
import com.hawk.sdufeforumpro.article.domain.entity.ArticleEs;
import com.hawk.sdufeforumpro.article.domain.entity.convertor.ArticleConvertor;
import com.hawk.sdufeforumpro.article.domain.service.ArticleService;
import com.hawk.sdufeforumpro.article.param.ArticleParam;
import com.hawk.sdufeforumpro.base.response.PageResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("article")
public class ArticleController {

    @Autowired
    private ArticleService articleService;

    @PostMapping("/addArticle")
    public Result<Boolean> addArticle(@Valid @RequestBody ArticleParam articleParam) {
        String userId = (String) StpUtil.getLoginId();

        ArticleAddRequest articleAddRequest = new ArticleAddRequest();
        articleAddRequest.setContent(articleParam.getContent());
        articleAddRequest.setUserId(userId);

        Boolean result = articleService.addArticle(articleAddRequest).getSuccess();
        return Result.success(result);
    }

    @GetMapping("/articleList")
    public MultiResult<ArticleInfo> getArticleList(String content, int currentPage, int pageSize) {
        ArticlePageQueryRequest articlePageQueryRequest = new ArticlePageQueryRequest();
        articlePageQueryRequest.setContent(content);
        articlePageQueryRequest.setCurrentPage(currentPage);
        articlePageQueryRequest.setPageSize(pageSize);

        PageResponse<ArticleEs> pageResponse = articleService.pageQueryByContent(articlePageQueryRequest);
        List<ArticleInfo> articleInfoList = pageResponse.getDatas().stream().map(ArticleConvertor.INSTANCE::mapToVo).toList();
        articleInfoList.forEach(articleInfo -> articleInfo.setLikeCount(articleService.getLikeCount(articleInfo.getArticleId().toString())));
        return MultiResult.successMulti(articleInfoList, pageResponse.getTotal(), currentPage, pageSize);
    }

    @GetMapping("/like")
    public Result<Boolean> likeArticle(String articleId) {
        String userId = (String) StpUtil.getLoginId();
        articleService.like(articleId, userId);
        return Result.success(true);
    }
}
