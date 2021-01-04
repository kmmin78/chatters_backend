package com.kms.chatters.chat.interceptor;

import java.security.Principal;
import java.util.Optional;

import com.kms.chatters.auth.service.UserDetailsServiceImpl;
import com.kms.chatters.chat.vo.ChatMessage;
import com.kms.chatters.common.utils.JwtUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.stomp.StompClientSupport;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;

@Component
public class StompInterceptor implements ChannelInterceptor{

    @Autowired
    private JwtUtils ju;

    @Autowired
	private UserDetailsServiceImpl userDetailsService;

    // @Autowired
    // SimpMessagingTemplate webSocket;

    private static final Logger logger = LoggerFactory.getLogger(StompInterceptor.class);

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel){
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);
        String token = accessor.getFirstNativeHeader("Authorization");
        System.out.println("token : " + token);
        StompCommand stc = accessor.getCommand();
        
        // System.out.println(accessor.getMessage());
        System.out.println("message : " + message.toString());
        String destination = accessor.getDestination();
        // System.out.println("destination : " + destination);
        //sessionId 이용하면 되겠다..
        //subscribe일 때 db에 roomid(topic), sessionid, userid 매핑해놓으면 입장, 퇴장 구현할 수 있을 듯.
        //대신 입장때는 topic <- 이건 destination으로 가져오네, userid (이건 token에서 꺼내자)는 header에 직접 추가해서 가져와야할듯?
        System.out.println("sessionid : " + accessor.getSessionId());

        

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
        return message;
    }
    
    
}
