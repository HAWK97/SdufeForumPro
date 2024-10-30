package com.hawk.sdufeforumpro.sms.service;

import com.hawk.sdufeforumpro.sms.response.SmsSendResponse;

/**
 * 短信服务
 *
 */
public interface SmsService {

    /**
     * 发送短信
     *
     * @param phoneNumber
     * @param code
     * @return
     */
    public SmsSendResponse sendMsg(String phoneNumber, String code);
}
