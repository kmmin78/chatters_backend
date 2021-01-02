package com.kms.chatters.chat.interceptor;

import java.security.Principal;
import java.util.Optional;

import com.kms.chatters.chat.vo.ChatMessage;
import com.kms.chatters.common.utils.JwtUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.stomp.StompClientSupport;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.stereotype.Component;

@Component
public class StompInterceptor implements ChannelInterceptor{

    @Autowired
    private JwtUtils ju;

    // @Autowired
    // SimpMessagingTemplate webSocket;

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel){
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);
        // String token = accessor.getFirstNativeHeader("Authorization");
        // System.out.println("token : " + token);
        StompCommand stc = accessor.getCommand();
        // System.out.println(accessor.getMessage());
        System.out.println("message : " + message.toString());
        String destination = accessor.getDestination();
        // System.out.println("destination : " + destination);
        //sessionId 이용하면 되겠다..
        //subscribe일 때 db에 roomid(topic), sessionid, userid 매핑해놓으면 입장, 퇴장 구현할 수 있을 듯.
        //대신 입장때는 topic <- 이건 destination으로 가져오네, userid (이건 token에서 꺼내자)는 header에 직접 추가해서 가져와야할듯?
        System.out.println("sessionid : " + accessor.getSessionId());
        // jwt 검증 로직 추가.
        switch(stc) {
            case CONNECT :

            //접속할 땐, JWT 검증을 거치고, JWT가 올바르지 않다면 멈춰야되는데.. 

            System.out.println("STOMP CONNECTED");
            // ju.validateJwtToken(token);
            break;

            case SUBSCRIBE :

            //접속 인원 +, 해당 방 유저들에게 입장했다고 알려야 함. destination, memberName 필요.
            
            System.out.println("STOMP SUBSCRIBED");
            break;

            case DISCONNECT :

            //접속 인원 -, 해당 방 유저들에게 퇴장했다고 알려야 함. destination, memberName 필요.

            System.out.println("STOMP DISCONNECTED");
            break;

            case SEND :

            System.out.println("STOMP SEND");
            break;

            default :

            break;
        }
        // if(StompCommand.CONNECT == stc){
        //     System.out.println("STOMP CONNECTED");
        //     // if(ju.validateJwtToken(token).equals("OK")){
        //     //     return message;
        //     // }
        // }else if(StompCommand.SUBSCRIBE == stc){
        //     System.out.println("STOMP SUBSCRIBED");
        //     //채팅방 인원 수 +

        //     // webSocket.convertAndSend(
        //     //     destination, 
        //     //     ChatMessage.builder().
        //     // );

        // }else if(StompCommand.DISCONNECT == stc){
        //     System.out.println("STOMP DISCONNECTED");
        //     //채팅방 인원 수 -

        // }else if(StompCommand.SEND == stc){
        //     System.out.println("STOMP SEND");
        // }

        // System.out.println("token : " + token);
        // 테스트 완료
        // System.out.println("클라이언트 인바운드 테스트");
        return message;
    }
    
    
}
