package com.hawk.sdufeforumpro.mq.param.eventMassage;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class EventModel {

    /**
     * 事件类型，用 EventType 枚举类表示，这里指该事件属于点赞还是评论
     */
    private EventType eventType;

    /**
     * 触发该事件的用户 id，这里指进行点赞或评论操作的用户 id
     */
    private Long actorId;

    /**
     * 被点赞或评论的对象类型，1 为动态，2 为评论
     */
    private EntityType entityType;

    /**
     * 被点赞或评论的对象 id
     */
    private Long entityId;

    /**
     * 被点赞或评论对象的拥有者 id，即将要收到私信的用户 id
     */
    private Long entityOwnerId;

    /**
     * 用于保存该事件的一些附加信息
     */
    private Map<String, String> exts = new HashMap<>();
}
