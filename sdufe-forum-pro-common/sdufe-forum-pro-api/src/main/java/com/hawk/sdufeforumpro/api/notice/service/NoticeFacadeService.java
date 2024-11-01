package com.hawk.sdufeforumpro.api.notice.service;

import com.hawk.sdufeforumpro.api.notice.response.NoticeResponse;

public interface NoticeFacadeService {

    /**
     * 生成并发送短信验证码
     *
     * @param telephone
     * @return
     */
    public NoticeResponse generateAndSendSmsCaptcha(String telephone);
}
