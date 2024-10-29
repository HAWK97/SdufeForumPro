package com.hawk.sdufeforumpro.mq.param;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 消息
 */
@Data
@Accessors(chain = true)
public class Message {

    /**
     * 消息id
     */
    private String msgId;

    /**
     * 消息体
     */
    private String body;
}
