package com.hawk.sdufeforumpro.user.facade;

import com.hawk.sdufeforumpro.api.user.request.UserModifyRequest;
import com.hawk.sdufeforumpro.api.user.request.UserPageQueryRequest;
import com.hawk.sdufeforumpro.api.user.request.UserQueryRequest;
import com.hawk.sdufeforumpro.api.user.request.UserRegisterRequest;
import com.hawk.sdufeforumpro.api.user.request.condition.UserIdQueryCondition;
import com.hawk.sdufeforumpro.api.user.request.condition.UserPhoneAndPasswordQueryCondition;
import com.hawk.sdufeforumpro.api.user.request.condition.UserPhoneQueryCondition;
import com.hawk.sdufeforumpro.api.user.response.UserOperatorResponse;
import com.hawk.sdufeforumpro.api.user.response.UserQueryResponse;
import com.hawk.sdufeforumpro.api.user.response.data.UserInfo;
import com.hawk.sdufeforumpro.api.user.service.UserFacadeService;
import com.hawk.sdufeforumpro.base.response.PageResponse;
import com.hawk.sdufeforumpro.rpc.facade.Facade;
import com.hawk.sdufeforumpro.user.domain.entity.User;
import com.hawk.sdufeforumpro.user.domain.entity.convertor.UserConvertor;
import com.hawk.sdufeforumpro.user.domain.service.UserService;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

@DubboService(version = "1.0.0")
public class UserFacadeServiceImpl implements UserFacadeService {

    @Autowired
    private UserService userService;

    @Facade
    @Override
    public UserQueryResponse<UserInfo> query(UserQueryRequest userQueryRequest) {
        User user = switch (userQueryRequest.getUserQueryCondition()) {
            case UserIdQueryCondition userIdQueryCondition:
                yield userService.findById(userIdQueryCondition.getUserId());
            case UserPhoneQueryCondition userPhoneQueryCondition:
                yield userService.findByTelephone(userPhoneQueryCondition.getTelephone());
            case UserPhoneAndPasswordQueryCondition userPhoneAndPasswordQueryCondition:
                yield userService.findByTelephoneAndPass(userPhoneAndPasswordQueryCondition.getTelephone(), userPhoneAndPasswordQueryCondition.getPassword());
            default:
                throw new UnsupportedOperationException(userQueryRequest.getUserQueryCondition() + "'' is not supported");
        };

        UserQueryResponse<UserInfo> response = new UserQueryResponse();
        response.setSuccess(true);
        UserInfo userInfo = UserConvertor.INSTANCE.mapToVo(user);
        response.setData(userInfo);
        return response;
    }

    @Override
    public PageResponse<UserInfo> pageQuery(UserPageQueryRequest userPageQueryRequest) {
        PageResponse<User> queryResult = userService.pageQueryByState(userPageQueryRequest.getKeyWord(), userPageQueryRequest.getState(), userPageQueryRequest.getCurrentPage(), userPageQueryRequest.getPageSize());
        PageResponse<UserInfo> response = new PageResponse<>();
        if (!queryResult.getSuccess()) {
            response.setSuccess(false);
            return response;
        }
        response.setSuccess(true);
        response.setDatas(UserConvertor.INSTANCE.mapToVo(queryResult.getDatas()));
        response.setCurrentPage(queryResult.getCurrentPage());
        response.setPageSize(queryResult.getPageSize());
        return response;
    }

    @Override
    public UserOperatorResponse register(UserRegisterRequest userRegisterRequest) {
        return userService.register(userRegisterRequest.getTelephone(), userRegisterRequest.getInviteCode());
    }

    @Override
    public UserOperatorResponse modify(UserModifyRequest userModifyRequest) {
        return null;
    }
}
