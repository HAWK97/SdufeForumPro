package com.hawk.sdufeforumpro.message.domain.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.hawk.sdufeforumpro.api.message.constant.MessageType;
import com.hawk.sdufeforumpro.api.message.constant.ReadState;
import com.hawk.sdufeforumpro.datasource.domain.entity.BaseEntity;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@TableName("messages")
public class Message extends BaseEntity {

    /**
     * 发送方 id
     */
    private Long fromId;

    /**
     * 接收方 id
     */
    private Long toId;

    /**
     * 内容
     */
    private String content;

    /**
     * 是否已阅
     */
    private ReadState readState;

    /**
     * 会话 id
     */
    private String conversationId;

    /**
     * 站内信类型
     */
    private MessageType messageType;
}
