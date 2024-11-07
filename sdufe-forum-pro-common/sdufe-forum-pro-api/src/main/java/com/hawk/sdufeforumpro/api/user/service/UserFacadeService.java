package com.hawk.sdufeforumpro.api.user.service;

import com.hawk.sdufeforumpro.api.user.request.UserModifyRequest;
import com.hawk.sdufeforumpro.api.user.request.UserPageQueryRequest;
import com.hawk.sdufeforumpro.api.user.request.UserQueryRequest;
import com.hawk.sdufeforumpro.api.user.request.UserRegisterRequest;
import com.hawk.sdufeforumpro.api.user.response.UserOperatorResponse;
import com.hawk.sdufeforumpro.api.user.response.UserQueryResponse;
import com.hawk.sdufeforumpro.api.user.response.data.UserInfo;
import com.hawk.sdufeforumpro.base.response.PageResponse;

public interface UserFacadeService {

    /**
     * 用户信息查询
     * @param userQueryRequest
     * @return
     */
    UserQueryResponse<UserInfo> query(UserQueryRequest userQueryRequest);

    /**
     * 分页查询用户信息
     * @param userPageQueryRequest
     * @return
     */
    PageResponse<UserInfo> pageQuery(UserPageQueryRequest userPageQueryRequest);

    /**
     * 用户注册
     * @param userRegisterRequest
     * @return
     */
    UserOperatorResponse register(UserRegisterRequest userRegisterRequest);

    /**
     * 用户信息修改
     * @param userModifyRequest
     * @return
     */
    UserOperatorResponse modify(UserModifyRequest userModifyRequest);
}
