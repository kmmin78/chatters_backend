package com.kms.chatters.chat.interceptor;

import javax.annotation.PostConstruct;

import com.kms.chatters.chat.dao.ChatRoomRepository;
import com.kms.chatters.chat.dao.ChatSessionRepository;
import com.kms.chatters.chat.redis.RedisPublisher;
import com.kms.chatters.chat.vo.ChatMessage;
import com.kms.chatters.common.utils.JwtUtils;

import org.springframework.context.annotation.DependsOn;
import org.springframework.lang.Nullable;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class StompInterceptor implements ChannelInterceptor{

    private final JwtUtils ju;
    // private final UserDetailsServiceImpl userDetailsService;
    private final ChatRoomRepository chatRoomRepository;
    private final ChatSessionRepository chatSessionRepository;
    private final RedisPublisher redisPublisher;

    // private static final Logger logger = LoggerFactory.getLogger(StompInterceptor.class);

    //인터셉터에서 연결, 연결해제 이벤트 감지해서 브로드캐스팅 하려고 했지만,
    //이 인터셉터 자체는 메세지 보낼 때마다 호출되어서 그런지, 브로드캐스팅이나 레디스에 세션 넣는
    //관련 로직이 들어가면 순환참조 혹은 에러 발생... 따라서 인터셉터가 아닌 웹소켓 세션 연결, 해제
    //이벤트 감지 하는 리스너를 통해서 작업할 예정.

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel){
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);
        StompCommand stc = accessor.getCommand();

        String session = accessor.getSessionId();
        String roomId = accessor.getFirstNativeHeader("roomId");
        String username = accessor.getFirstNativeHeader("username");
        String memberName = accessor.getFirstNativeHeader("memberName");
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

            System.out.println("STOMP CONNECTED");

            break;

            case SUBSCRIBE :

            System.out.println(session + " : " + roomId + " : " + username + " : " + memberName);
            
            //세션 저장
            // chatSessionRepository.setSession(session, roomId, username, memberName);
            //접속 인원 +, 해당 방 유저들에게 입장했다고 알려야 함. destination, memberName 필요.
            //역시 에러...
            // redisPublisher.publish(
            //     chatRoomRepository.getTopic(roomId), 
            //     ChatMessage
            //         .builder()
            //         .roomId(roomId)
            //         .username(username)
            //         .memberName(memberName)
            //         .type(ChatMessage.MessageType.ENTER)
            //         .message(memberName + "님이 입장하였습니다.")
            //         .sendDate("")
            //         .build()
            // );
            
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
        return message;
    }
    
}
