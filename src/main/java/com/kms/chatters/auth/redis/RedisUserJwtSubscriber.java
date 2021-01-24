package com.kms.chatters.auth.redis;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kms.chatters.auth.vo.UserJwtMessage;

import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class RedisUserJwtSubscriber {
    
    private final ObjectMapper objectMapper;
    private final SimpMessageSendingOperations messagingTemplate;

     /**
     * Redis에서 메시지가 발행(publish)되면 대기하고 있던 Redis Subscriber가 해당 메시지를 받아 처리한다.
     */
    public void sendMessage(String publishMessage) {
        try {
            UserJwtMessage userJwtMessage = objectMapper.readValue(publishMessage, UserJwtMessage.class);
            // 해당 username을 갖고 있는 클라이언트에게 메시지 발송 (stomp publish)
            messagingTemplate.convertAndSend("/topic/ccu/" + userJwtMessage.getUsername(), userJwtMessage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
