package com.kms.chatters.chat.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.messaging.simp.SimpMessageType;
import org.springframework.security.config.annotation.web.messaging.MessageSecurityMetadataSourceRegistry;
import org.springframework.security.config.annotation.web.socket.AbstractSecurityWebSocketMessageBrokerConfigurer;

@Configuration
// @Order(Ordered.HIGHEST_PRECEDENCE + 99)
public class WebSocketSecurityConfig extends AbstractSecurityWebSocketMessageBrokerConfigurer  {
    
    @Override
    protected void configureInbound(MessageSecurityMetadataSourceRegistry messages) {
        // 현재 websocket security 적용이 제대로 안됨.. 흠. 추후 확인 필요.
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
