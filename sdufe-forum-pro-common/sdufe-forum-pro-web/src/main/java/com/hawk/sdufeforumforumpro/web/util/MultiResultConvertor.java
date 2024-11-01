package com.hawk.sdufeforumforumpro.web.util;

import com.hawk.sdufeforumforumpro.web.vo.MultiResult;
import com.hawk.sdufeforumpro.base.response.PageResponse;

import static com.hawk.sdufeforumpro.base.response.ResponseCode.SUCCESS;

public class MultiResultConvertor {

    public static <T> MultiResult<T> convert(PageResponse<T> pageResponse) {
        MultiResult<T> multiResult = new MultiResult<T>(true, SUCCESS.name(), SUCCESS.name(), pageResponse.getDatas(), pageResponse.getTotal(), pageResponse.getCurrentPage(), pageResponse.getPageSize());
        return multiResult;
    }
}
