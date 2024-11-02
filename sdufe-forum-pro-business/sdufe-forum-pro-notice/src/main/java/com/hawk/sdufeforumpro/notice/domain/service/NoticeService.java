package com.hawk.sdufeforumpro.notice.domain.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hawk.sdufeforumpro.base.exception.BizException;
import com.hawk.sdufeforumpro.notice.domain.constant.NoticeState;
import com.hawk.sdufeforumpro.notice.domain.constant.NoticeType;
import com.hawk.sdufeforumpro.notice.domain.entity.Notice;
import com.hawk.sdufeforumpro.notice.infrastructure.NoticeMapper;
import org.springframework.stereotype.Service;

import static com.hawk.sdufeforumpro.base.exception.BizErrorCode.NOTICE_SAVE_FAILED;

@Service
public class NoticeService extends ServiceImpl<NoticeMapper, Notice> {

    private static final String SMS_NOTICE_TITLE = "验证码";

    public Page<Notice> pageQueryForRetry(int currentPage, int pageSize) {
        Page<Notice> page = new Page<>(currentPage, pageSize);
        QueryWrapper<Notice> wrapper = new QueryWrapper<>();
        wrapper.in("state", NoticeState.INIT, NoticeState.FAILED);

        wrapper.orderBy(true, true, "gmt_create");

        return this.page(page, wrapper);
    }

    public Notice saveCaptcha(String telephone, String captcha) {
        Notice notice = Notice.builder()
                .noticeTitle(SMS_NOTICE_TITLE)
                .noticeContent(captcha)
                .noticeType(NoticeType.SMS)
                .targetAddress(telephone)
                .state(NoticeState.INIT)
                .build();

        Boolean saveResult = this.save(notice);

        if (!saveResult) {
            throw new BizException(NOTICE_SAVE_FAILED);
        }

        return notice;
    }
}
