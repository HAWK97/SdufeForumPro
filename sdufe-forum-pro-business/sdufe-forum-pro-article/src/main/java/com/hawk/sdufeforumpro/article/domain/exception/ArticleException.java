package com.hawk.sdufeforumpro.article.domain.exception;

import com.hawk.sdufeforumpro.base.exception.BizException;
import com.hawk.sdufeforumpro.base.exception.ErrorCode;

public class ArticleException extends BizException {

    public ArticleException(ErrorCode errorCode) {
        super(errorCode);
    }
}
