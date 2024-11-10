package com.hawk.sdufeforumpro.ws.service;

import jakarta.websocket.OnClose;
import jakarta.websocket.OnOpen;
import jakarta.websocket.Session;
import jakarta.websocket.server.PathParam;
import jakarta.websocket.server.ServerEndpoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;

@ServerEndpoint("/websocket/{userId}")
@Service
public class WebSocketService {

    private static final Logger LOG = LoggerFactory.getLogger(WebSocketService.class);

    private static ConcurrentHashMap<String, Session> userSessionMap = new ConcurrentHashMap<>();

    private static ApplicationContext applicationContext;

    public static void setApplicationContext(ApplicationContext context) {
        applicationContext = context;
    }

    @OnOpen
    public void onOpen(Session session, @PathParam("userId") String userId) {
        userSessionMap.put(userId, session);
        LOG.info("connection established, userId={}", userId);
    }

    @OnClose
    public void onClose() {
        LOG.info("connection closed");
    }

    public void sendMessage(String userId, String message) throws IOException {
        if (userSessionMap.containsKey(userId)) {
            Session session = userSessionMap.get(userId);
            if (session != null) {
                session.getBasicRemote().sendText(message);
            }
        }
    }
}
