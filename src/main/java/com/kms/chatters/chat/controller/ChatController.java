package com.kms.chatters.chat.controller;

import com.kms.chatters.chat.vo.ChatMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
// import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
// import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ChatController {

    @Autowired
    SimpMessagingTemplate webSocket;
    
    @MessageMapping("/sendToAll")
    // @SendTo("/topic/all")
    public void sendToAll(ChatMessage message) {
        // return message.getMessage();
        if(message.getType().equals("ENTER")){
            message.setMessage(message.getUser()+"님이 입장하였습니다.");
        }
        if(message.getType().equals("EXIT")){
            message.setMessage(message.getUser()+"님이 퇴장하였습니다.");
        }
        webSocket.convertAndSend("/topic/all", message);
    }
    

}
