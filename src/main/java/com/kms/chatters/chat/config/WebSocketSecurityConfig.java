package com.kms.chatters.chat.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.messaging.MessageSecurityMetadataSourceRegistry;
import org.springframework.security.config.annotation.web.socket.AbstractSecurityWebSocketMessageBrokerConfigurer;

@Configuration
// @Order(Ordered.HIGHEST_PRECEDENCE + 99)
public class WebSocketSecurityConfig extends AbstractSecurityWebSocketMessageBrokerConfigurer  {
    
    @Override
    protected void configureInbound(MessageSecurityMetadataSourceRegistry messages) {
        // 현재 websocket security 적용이 제대로 안됨. 공식문서도 여기만 설정하면 보안이 된다고하는데...
        // 거르는 건 여기서 해주는게 맞는데, 그럼 인증 처리는 어디서 한다는거지????
        // messages.simpTypeMatchers(
        //     SimpMessageType.CONNECT,
        //     SimpMessageType.SUBSCRIBE,
        //     SimpMessageType.MESSAGE,
        //     SimpMessageType.UNSUBSCRIBE, 
        //     SimpMessageType.DISCONNECT, 
        //     SimpMessageType.HEARTBEAT
        // ).permitAll()
        // .simpDestMatchers("/group/**", "/topic/**").authenticated()
        // .simpSubscribeDestMatchers("/topic/**").authenticated()
        // .anyMessage().denyAll();
        // messages.anyMessage().authenticated();
        messages.anyMessage().permitAll();
    }

    @Override
    protected boolean sameOriginDisabled() {
        return true;
    }
}
