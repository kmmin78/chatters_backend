package com.kms.chatters.chat.redis;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kms.chatters.chat.vo.ChatMessage;

import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RedisSubscriber implements MessageListener{

    private final ObjectMapper objectMapper;
    private final RedisTemplate<String, Object> redisTemplate;
    private final SimpMessagingTemplate webSocket;

    @Override
	public void onMessage(Message message, byte[] pattern) {
		try {
            String publishMessage 
                = (String) redisTemplate.getStringSerializer()
                                        .deserialize(message.getBody());
            ChatMessage chatMessage = objectMapper.readValue(publishMessage, ChatMessage.class);

            webSocket.convertAndSend("/topic/" + chatMessage.getRoomId(), chatMessage);

        } catch (Exception e) {
            e.printStackTrace();
        }
	}
    
}
