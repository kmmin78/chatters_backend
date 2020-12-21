package com.kms.chatters.chat.controller;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ChatController {
    
    @MessageMapping("/testSend")
    @SendTo("/topic/testRoom")
    public String testMessage() {
        return "test message";
    }
    

}
