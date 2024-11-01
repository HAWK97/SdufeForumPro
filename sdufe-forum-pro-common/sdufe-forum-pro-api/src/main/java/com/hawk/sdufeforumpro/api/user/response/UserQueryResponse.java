package com.hawk.sdufeforumpro.api.user.response;

import com.hawk.sdufeforumpro.base.response.BaseResponse;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class UserQueryResponse<T> extends BaseResponse {

    private static final long serialVersionUID = 1L;

    private T data;
}
