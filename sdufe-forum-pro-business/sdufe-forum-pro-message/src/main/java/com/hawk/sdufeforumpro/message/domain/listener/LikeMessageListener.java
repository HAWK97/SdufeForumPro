package com.hawk.sdufeforumpro.message.domain.listener;

import com.alibaba.fastjson2.JSON;
import com.hawk.sdufeforumpro.mq.param.MessageBody;
import com.hawk.sdufeforumpro.mq.param.eventMassage.EventModel;
import com.hawk.sdufeforumpro.ws.service.WebSocketService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.function.Consumer;

@Component
@Slf4j
public class LikeMessageListener {

    @Autowired
    private WebSocketService webSocketService;

    @Bean
    Consumer<Message<MessageBody>> like() {
        return message -> {
            EventModel eventModel = JSON.parseObject(message.getPayload().getBody(), EventModel.class);
            log.info("received like event: {}", eventModel);

            try {
                webSocketService.sendMessage(eventModel.getEntityOwnerId().toString(), JSON.toJSONString(eventModel));
            } catch (IOException e) {
                log.error(e.getMessage());
            }
        };
    }
}
