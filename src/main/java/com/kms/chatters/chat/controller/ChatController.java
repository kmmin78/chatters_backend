package com.kms.chatters.chat.controller;

import com.kms.chatters.chat.dao.ChatRoomRepository;
import com.kms.chatters.chat.redis.RedisPublisher;
import com.kms.chatters.chat.vo.ChatMessage;
import com.kms.chatters.chat.vo.ChatRoom;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
// import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
// import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
public class ChatController {
    
    private final SimpMessagingTemplate webSocket;
    private final RedisPublisher redisPublisher;
    private final ChatRoomRepository chatRoomRepository;
    private final RedisTemplate<String, Object> redisTemplate;

    
    @MessageMapping("/send")
    // @SendTo("/topic/all")
    public void send(ChatMessage message, @Header("Authorization") String token) {
        // return message.getMessage();
        // if(message.getType().equals("ENTER")){
        //     message.setMessage(message.getMemberName()+"님이 입장하였습니다.");
        // }
        // if(message.getType().equals("EXIT")){
        //     message.setMessage(message.getMemberName()+"님이 퇴장하였습니다.");
        // }
        // System.out.println(token);
        // webSocket.convertAndSend("/topic/" + message.getRoomId(), message);

        //redisPublisher 삭제해야할듯??
        redisPublisher.publish(chatRoomRepository.getTopic(message.getRoomId()), message);
    }

    //스프링 서버 -> redis 서버 구독
    @GetMapping("/enter")
    public String enter(@RequestParam String roomId) {
        chatRoomRepository.enterChatRoom(roomId);
        return "OK";
    }

    @GetMapping("/testRoom")
    public ChatRoom testRoom() {
        HashOperations<String, String, ChatRoom> chatRoomHashOps = redisTemplate.opsForHash();
        return chatRoomHashOps.get("CHAT_ROOMS", "all");
    }

    //아래 enter, exit은 화면의 스크립트가 동작하면 정상작동하지만,
    //생각지 못했던 상황 (브라우저 종료, 비정상적인 종료 등)일 경우
    //스크립트가 실행되지 않기 때문에, 해당 내용이 publish되지 않는다.
    //그래서 StompInterceptor에서 해당 처리를 할 수 있도록 구현해야 한다.
    //추후 삭제해야 된다는 말.
    
    @MessageMapping("/all/enter")
    public void allEnter(ChatMessage message, @Header("Authorization") String token){
        message.setMessage(message.getMemberName()+"님이 입장하였습니다.");
        webSocket.convertAndSend("/topic/all", message);
    }
    
    @MessageMapping("/all/exit")
    public void allExit(ChatMessage message, @Header("Authorization") String token){
        message.setMessage(message.getMemberName()+"님이 퇴장하였습니다.");
        webSocket.convertAndSend("/topic/all", message);
    }

}
