package com.hawk.sdufeforumpro.api.user.response;

import com.hawk.sdufeforumpro.api.user.response.data.UserInfo;
import com.hawk.sdufeforumpro.base.response.BaseResponse;
import lombok.Getter;
import lombok.Setter;

/**
 * 用户操作响应
 */
@Getter
@Setter
public class UserOperatorResponse extends BaseResponse {

    private UserInfo user;
}
