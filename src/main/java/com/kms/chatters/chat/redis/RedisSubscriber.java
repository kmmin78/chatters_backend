package com.kms.chatters.chat.redis;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kms.chatters.chat.vo.ChatMessage;

import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class RedisSubscriber {

    private final ObjectMapper objectMapper;
    private final SimpMessageSendingOperations messagingTemplate;

    /**
     * Redis에서 메시지가 발행(publish)되면 대기하고 있던 Redis Subscriber가 해당 메시지를 받아 처리한다.
     */
    public void sendMessage(String publishMessage) {
        try {
            // ChatMessage 객채로 맵핑
            ChatMessage chatMessage = objectMapper.readValue(publishMessage, ChatMessage.class);
            // 채팅방을 구독한 클라이언트에게 메시지 발송 (stomp publish)
            messagingTemplate.convertAndSend("/topic/" + chatMessage.getRoomId(), chatMessage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}


// @Service
// @RequiredArgsConstructor
// public class RedisSubscriber implements MessageListener{

//     private final ObjectMapper objectMapper;
//     private final RedisTemplate<String, Object> redisTemplate;
//     private final SimpMessagingTemplate webSocket;

//     @Override
// 	public void onMessage(Message message, byte[] pattern) {
// 		try {
//             String publishMessage 
//                 = (String) redisTemplate.getStringSerializer()
//                                         .deserialize(message.getBody());
//             ChatMessage chatMessage = objectMapper.readValue(publishMessage, ChatMessage.class);

//             webSocket.convertAndSend("/topic/" + chatMessage.getRoomId(), chatMessage);

//         } catch (Exception e) {
//             e.printStackTrace();
//         }
// 	}
    
// }