package com.hawk.sdufeforumpro.notice.facade;

import cn.hutool.core.util.RandomUtil;
import com.alibaba.fastjson.JSON;
import com.hawk.sdufeforumpro.api.notice.response.NoticeResponse;
import com.hawk.sdufeforumpro.api.notice.service.NoticeFacadeService;
import com.hawk.sdufeforumpro.base.exception.SystemException;
import com.hawk.sdufeforumpro.base.utils.ThreadPoolUtils;
import com.hawk.sdufeforumpro.limiter.SlidingWindowRateLimiter;
import com.hawk.sdufeforumpro.notice.domain.constant.NoticeState;
import com.hawk.sdufeforumpro.notice.domain.entity.Notice;
import com.hawk.sdufeforumpro.notice.domain.service.NoticeService;
import com.hawk.sdufeforumpro.rpc.facade.Facade;
import com.hawk.sdufeforumpro.sms.response.SmsSendResponse;
import com.hawk.sdufeforumpro.sms.service.SmsService;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.util.Date;
import java.util.concurrent.TimeUnit;

import static com.hawk.sdufeforumpro.api.notice.constant.NoticeConstant.CAPTCHA_KEY_PREFIX;
import static com.hawk.sdufeforumpro.base.exception.BizErrorCode.SEND_NOTICE_DUPLICATED;

@DubboService(version = "1.0.0")
public class NoticeFacadeServiceImpl implements NoticeFacadeService {

    @Autowired
    private SlidingWindowRateLimiter slidingWindowRateLimiter;

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    private NoticeService noticeService;

    @Autowired
    private SmsService smsService;

    /**
     * 生成并发送短信验证码
     *
     * @param telephone
     * @return
     */
    @Facade
    @Override
    public NoticeResponse generateAndSendSmsCaptcha(String telephone) {
        Boolean access = slidingWindowRateLimiter.tryAcquire(telephone, 1, 60);

        if (!access) {
            throw new SystemException(SEND_NOTICE_DUPLICATED);
        }

        // 生成验证码
        String captcha = RandomUtil.randomNumbers(4);

        // 验证码存入Redis
        redisTemplate.opsForValue().set(CAPTCHA_KEY_PREFIX + telephone, captcha, 5, TimeUnit.MINUTES);

        Notice notice = noticeService.saveCaptcha(telephone, captcha);

        ThreadPoolUtils.getSmsSendPool().execute(() -> {
            SmsSendResponse result = smsService.sendMsg(notice.getTargetAddress(), notice.getNoticeContent());
            if (result.getSuccess()) {
                notice.setState(NoticeState.SUCCESS);
                notice.setSendSuccessTime(new Date());
                noticeService.updateById(notice);
            } else {
                notice.setState(NoticeState.FAILED);
                notice.addExtendInfo("executeResult", JSON.toJSONString(result));
                noticeService.updateById(notice);
            }
        });

        return new NoticeResponse.Builder().setSuccess(true).build();
    }
}
