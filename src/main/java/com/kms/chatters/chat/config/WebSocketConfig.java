package com.kms.chatters.chat.config;

import com.kms.chatters.chat.interceptor.StompInterceptor;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSocketMessageBroker
@RequiredArgsConstructor
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer{

    private final StompInterceptor stompInterceptor;

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        config.enableSimpleBroker("/topic", "/queue");
        config.setApplicationDestinationPrefixes("/topic");
        // config.setUserDestinationPrefix("/user");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/chatting", "/ccu")
                // .setAllowedOrigins("http://localhost:3000")
                .setAllowedOrigins("*")
                .withSockJS();
    }

    //클라이언트에서 request할 때 interceptor 등록
    @Override
    public void configureClientInboundChannel(ChannelRegistration registration){
        // registration.interceptors(stompInterceptor());
        registration.interceptors(stompInterceptor);
    }
    
}
