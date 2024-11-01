package com.hawk.sdufeforumpro.api.user.request;

import com.hawk.sdufeforumpro.api.user.request.condition.UserIdQueryCondition;
import com.hawk.sdufeforumpro.api.user.request.condition.UserPhoneAndPasswordQueryCondition;
import com.hawk.sdufeforumpro.api.user.request.condition.UserPhoneQueryCondition;
import com.hawk.sdufeforumpro.api.user.request.condition.UserQueryCondition;
import com.hawk.sdufeforumpro.base.request.BaseRequest;
import lombok.*;

@Setter
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class UserQueryRequest extends BaseRequest {

    private UserQueryCondition userQueryCondition;

    public UserQueryRequest(Long userId) {
        UserIdQueryCondition userIdQueryCondition;
        userIdQueryCondition = new UserIdQueryCondition();
        userIdQueryCondition.setUserId(userId);
        this.userQueryCondition = userIdQueryCondition;
    }

    public UserQueryRequest(String telephone) {
        UserPhoneQueryCondition userPhoneQueryCondition = new UserPhoneQueryCondition();
        userPhoneQueryCondition.setTelephone(telephone);
        this.userQueryCondition = userPhoneQueryCondition;
    }

    public UserQueryRequest(String telephone, String password) {
        UserPhoneAndPasswordQueryCondition userPhoneAndPasswordQueryCondition = new UserPhoneAndPasswordQueryCondition();
        userPhoneAndPasswordQueryCondition.setTelephone(telephone);
        userPhoneAndPasswordQueryCondition.setPassword(password);
        this.userQueryCondition = userPhoneAndPasswordQueryCondition;
    }
}
