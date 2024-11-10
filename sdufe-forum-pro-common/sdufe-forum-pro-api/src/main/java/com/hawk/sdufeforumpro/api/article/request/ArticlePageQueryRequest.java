package com.hawk.sdufeforumpro.api.article.request;

import lombok.*;

@Setter
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class ArticlePageQueryRequest {

    /**
     * 文章内容
     */
    private String content;

    /**
     * 当前页
     */
    private int currentPage;

    /**
     * 页面大小
     */
    private int pageSize;
}
