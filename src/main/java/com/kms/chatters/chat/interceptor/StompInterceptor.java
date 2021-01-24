package com.kms.chatters.chat.interceptor;

import java.util.Optional;

import com.kms.chatters.chat.service.ChatService;
import com.kms.chatters.chat.vo.ChatMessage;

import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component
public class StompInterceptor implements ChannelInterceptor{

    // private final JwtUtils ju;
    // private final UserDetailsServiceImpl userDetailsService;
    private final ChatService chatService;

    // private static final Logger logger = LoggerFactory.getLogger(StompInterceptor.class);

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel){
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);
        StompCommand stc = accessor.getCommand();

        String session = accessor.getSessionId();
        String roomId, username, memberName;

        // System.out.println(accessor.getDestination());
        // System.out.println(session);
        
        // String token = accessor.getFirstNativeHeader("Authorization");
        // String destination = accessor.getDestination();

        // jwt 검증 로직 추가 필요.
        switch(stc) {
            case CONNECT :

                //websocket security로 해봤는데, 잘 안된다. 분명 accessor에 setUser만 잘하면 다음 요청부터는 인증 안해도 될텐데.. 흠.
                //심지어 CONNECT authenticated로 해놓으면 이 부분을 타지도 않음.

                //유효한 jwt일 경우
                // String jwt = ju.parseJwt(token);
                // String validResult = "";
                // if(jwt != null){
                //     validResult = ju.validateJwtToken(jwt);
                // }
                // if (validResult.equals("OK")) {
                //     try {
                        
                //         String username = ju.getUserNameFromJwtToken(jwt);

                //         UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                //         UsernamePasswordAuthenticationToken authentication = 
                //             new UsernamePasswordAuthenticationToken(
                //                 userDetails, 
                //                 null, 
                //                 userDetails.getAuthorities()
                //             );
                            
                //         accessor.setUser(authentication);
                //     } catch (Exception e) {
                //         logger.error("Cannot set user authentication: {}", e);
                //     }
                // }

                // System.out.println("STOMP CONNECTED");

                break;

            case SUBSCRIBE :

                roomId = chatService.getRoomId(accessor.getDestination());
                username = accessor.getFirstNativeHeader("username");
                memberName = accessor.getFirstNativeHeader("memberName");

                // System.out.println(session + " : " + roomId + " : " + username + " : " + memberName);
                //채팅방 접속일 경우
                if(roomId.equals("all")){
                    //세션 저장
                    chatService.setSession(session, roomId, username, memberName);
                    //접속 수 +1
                    chatService.increaseUserCount(roomId);
                    //입장 메세지 전송
                    chatService.sendChatMessage(
                        ChatMessage
                        .builder()
                        .roomId(roomId)
                        .username(username)
                        .memberName(memberName)
                        .type(ChatMessage.MessageType.ENTER)
                        .message("[시스템] " + memberName + "님이 입장하였습니다.")
                        .sendDate("")
                        .build()
                    );
                }else if(roomId.equals("ccu")){
                    System.out.println(accessor.getDestination());
                    System.out.println(session);
                }
            
                // System.out.println("STOMP SUBSCRIBED");
                break;

            case DISCONNECT :

            // System.out.println(accessor.getDestination());
            // System.out.println(session);
                
                //채팅 세션 처리
                Optional
                    .ofNullable(chatService.getSession(session))
                    .ifPresent(
                        chatSession -> {
                            String innerRoomId = chatSession.getRoomId();
                            String innerUsername = chatSession.getUsername();
                            String innerMemberName = chatSession.getMemberName();
        
                            //해당 세션 삭제
                            chatService.deleteSession(session);
                            //접속 수 -1
                            chatService.decreaseUserCount(innerRoomId);
        
                            //접속 인원 -, 해당 방 유저들에게 퇴장했다고 알려야 함. destination, memberName 필요.
                            chatService.sendChatMessage(
                                ChatMessage
                                .builder()
                                .roomId(innerRoomId)
                                .username(innerUsername)
                                .memberName(innerMemberName)
                                .type(ChatMessage.MessageType.EXIT)
                                .message("[시스템] " + innerMemberName + "님이 퇴장하였습니다.")
                                .sendDate("")
                                .build()
                            );
                        }
                    );
                
                //로그인 세션 처리

                //세션 가져오기
                // ChatSession chatSession = chatService.getSession(session);
                // if(chatSession != null){
                //     roomId = chatSession.getRoomId();
                //     username = chatSession.getUsername();
                //     memberName = chatSession.getMemberName();

                //     //해당 세션 삭제
                //     chatService.deleteSession(session);
                //     //접속 수 -1
                //     chatService.decreaseUserCount(roomId);

                //     //접속 인원 -, 해당 방 유저들에게 퇴장했다고 알려야 함. destination, memberName 필요.
                //     chatService.sendChatMessage(
                //         ChatMessage
                //         .builder()
                //         .roomId(roomId)
                //         .username(username)
                //         .memberName(memberName)
                //         .type(ChatMessage.MessageType.EXIT)
                //         .message("[시스템] " + memberName + "님이 퇴장하였습니다.")
                //         .sendDate("")
                //         .build()
                //     );
                // }

                // System.out.println("STOMP DISCONNECTED");
                break;

            case SEND :

                // System.out.println("STOMP SEND");
                break;

            default :

                break;
        }
        return message;
    }
    
}
