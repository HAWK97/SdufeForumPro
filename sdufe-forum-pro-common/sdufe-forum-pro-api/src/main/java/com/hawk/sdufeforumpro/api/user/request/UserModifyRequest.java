package com.hawk.sdufeforumpro.api.user.request;

import com.hawk.sdufeforumpro.base.request.BaseRequest;
import jakarta.validation.constraints.NotNull;
import lombok.*;

/**
 * @author Hollis
 */
@Setter
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class UserModifyRequest extends BaseRequest {

    @NotNull(message = "userId不能为空")
    private Long userId;

    private String nickName;

    private String password;

    private String introduce;

    private String profilePhotoUrl;

    private String telephone;
}
