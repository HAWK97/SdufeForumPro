package com.hawk.sdufeforumpro.article.domain.exception;

import com.hawk.sdufeforumpro.base.exception.ErrorCode;

public enum ArticleErrorCode implements ErrorCode {

    /**
     * 文章操作失败
     */
    ARTICLE_OPERATE_FAILED("ARTICLE_OPERATE_FAILED", "文章操作失败"),

    /**
     * 点赞过于频繁
     */
    LIKE_TOO_FREQUENT("LIKE_TOO_FREQUENT", "点赞过于频繁");

    private String code;

    private String message;

    ArticleErrorCode(String code, String message) {
        this.code = code;
        this.message = message;
    }

    @Override
    public String getCode() {
        return this.code;
    }

    @Override
    public String getMessage() {
        return this.message;
    }
}
