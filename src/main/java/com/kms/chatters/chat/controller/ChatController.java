package com.kms.chatters.chat.controller;

import com.kms.chatters.chat.service.ChatService;
import com.kms.chatters.chat.vo.ChatMessage;

import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
public class ChatController {
    
    private final ChatService chatService;
    
    @MessageMapping("/send")
    // @SendTo("/topic/all")
    public void send(ChatMessage message, @Header("Authorization") String token) {
        //메세지 전송
        chatService.sendChatMessage(message);
    }

}
