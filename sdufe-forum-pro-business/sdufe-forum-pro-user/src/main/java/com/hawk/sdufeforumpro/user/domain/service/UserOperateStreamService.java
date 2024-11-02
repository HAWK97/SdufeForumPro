package com.hawk.sdufeforumpro.user.domain.service;

import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hawk.sdufeforumpro.api.user.constant.UserOperateTypeEnum;
import com.hawk.sdufeforumpro.user.domain.entity.User;
import com.hawk.sdufeforumpro.user.domain.entity.UserOperateStream;
import com.hawk.sdufeforumpro.user.infrastructure.mapper.UserOperateStreamMapper;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class UserOperateStreamService extends ServiceImpl<UserOperateStreamMapper, UserOperateStream> {

    public Long insertStream(User user, UserOperateTypeEnum type) {
        UserOperateStream stream = new UserOperateStream();
        stream.setUserId(String.valueOf(user.getId()));
        stream.setOperateTime(new Date());
        stream.setType(type.name());
        stream.setParam(JSON.toJSONString(user));
        boolean result = save(stream);
        if (result) {
            return stream.getId();
        }
        return null;
    }
}
