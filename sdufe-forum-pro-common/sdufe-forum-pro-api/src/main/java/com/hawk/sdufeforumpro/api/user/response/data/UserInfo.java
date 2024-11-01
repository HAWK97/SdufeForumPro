package com.hawk.sdufeforumpro.api.user.response.data;

import com.hawk.sdufeforumpro.api.user.constant.UserRole;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
public class UserInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 用户Id
     */
    private Long userId;

    /**
     * 昵称
     */
    private String nickName;

    /**
     * 手机号
     */
    private String telephone;

    /**
     * 状态
     */
    private String state;

    /**
     * 头像地址
     */
    private String profilePhotoUrl;

    /**
     * 用户角色
     */
    private UserRole userRole;

    /**
     * 邀请码
     */
    private String inviteCode;

    /**
     * 注册时间
     */
    private Date createTime;
}
