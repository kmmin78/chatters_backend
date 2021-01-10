package com.kms.chatters.chat.config;

import com.kms.chatters.chat.dao.ChatRoomRepository;
import com.kms.chatters.chat.dao.ChatSessionRepository;
import com.kms.chatters.chat.interceptor.StompInterceptor;
import com.kms.chatters.chat.redis.RedisPublisher;
import com.kms.chatters.common.utils.JwtUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer{

    //interceptor에서 @Component 달고 바로 주입하면
    //인터셉터 빈 생성할 때 repository 빈이 생성되지 않은 상태 발생.
    //따라서, Config에서 인터셉터 생성 및 생성자 주입시켜서 주입한다.
    // 이것도 지금 실패다...
    @Autowired
    private JwtUtils ju;
    @Autowired
    private ChatRoomRepository chatRoomRepository;
    @Autowired
    private ChatSessionRepository chatSessionRepository;
    @Autowired
    private RedisPublisher redisPublisher;
    
    @Bean
    @Order(Ordered.HIGHEST_PRECEDENCE + 99)
    public StompInterceptor stompInterceptor() {
        // return new StompInterceptor(ju);
        return new StompInterceptor(ju, chatRoomRepository, chatSessionRepository, redisPublisher);
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        config.enableSimpleBroker("/topic", "/queue");
        config.setApplicationDestinationPrefixes("/topic");
        // config.setUserDestinationPrefix("/user");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/chatters")
                // .setAllowedOrigins("http://localhost:3000")
                .setAllowedOrigins("*")
                .withSockJS();
    }

    //클라이언트에서 request할 때 interceptor 등록
    @Override
    public void configureClientInboundChannel(ChannelRegistration registration){
        registration.interceptors(stompInterceptor());
        
    }
    
}
