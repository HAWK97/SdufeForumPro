package com.hawk.sdufeforumpro.api.user.request;

import com.hawk.sdufeforumpro.base.request.BaseRequest;
import lombok.*;

@Setter
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class UserRegisterRequest extends BaseRequest {

    private String telephone;

    private String inviteCode;

    private String password;
}
