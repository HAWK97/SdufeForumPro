package com.hawk.sdufeforumpro.api.user.response.data;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class InviteRankInfo {

    /**
     * 昵称
     */
    private String nickName;

    /**
     * 邀请码
     */
    private String inviteCode;

    /**
     * 邀请积分
     */
    private Integer inviteScore;
}
