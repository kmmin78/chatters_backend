package com.kms.chatters.chat.controller;

import com.kms.chatters.chat.vo.ChatMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
// import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
// import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ChatController {
    
    @Autowired
    SimpMessagingTemplate webSocket;
    
    @MessageMapping("/all/send")
    // @SendTo("/topic/all")
    public void allSend(ChatMessage message, @Header("accessToken") String token) {
        // return message.getMessage();
        // if(message.getType().equals("ENTER")){
        //     message.setMessage(message.getMemberName()+"님이 입장하였습니다.");
        // }
        // if(message.getType().equals("EXIT")){
        //     message.setMessage(message.getMemberName()+"님이 퇴장하였습니다.");
        // }
        System.out.println(token);
        webSocket.convertAndSend("/topic/all", message);
    }


    //아래 enter, exit은 화면의 스크립트가 동작하면 정상작동하지만,
    //생각지 못했던 상황 (브라우저 종료, 비정상적인 종료 등)일 경우
    //스크립트가 실행되지 않기 때문에, 해당 내용이 publish되지 않는다.
    //그래서 StompInterceptor에서 해당 처리를 할 수 있도록 구현해야 한다.
    //추후 삭제해야 된다는 말.

    @MessageMapping("/all/enter")
    public void allEnter(ChatMessage message, @Header("accessToken") String token){
        message.setMessage(message.getMemberName()+"님이 입장하였습니다.");
        webSocket.convertAndSend("/topic/all", message);
    }
    
    @MessageMapping("/all/exit")
    public void allExit(ChatMessage message, @Header("accessToken") String token){
        message.setMessage(message.getMemberName()+"님이 퇴장하였습니다.");
        webSocket.convertAndSend("/topic/all", message);
    }

}
