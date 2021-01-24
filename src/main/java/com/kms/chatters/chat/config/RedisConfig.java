package com.kms.chatters.chat.config;

import com.kms.chatters.chat.redis.RedisSubscriber;
import com.kms.chatters.auth.redis.RedisUserJwtSubscriber;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;


@Configuration
public class RedisConfig {

    @Value("${spring.redis.host}")
    private String host;
    @Value("${spring.redis.port}")
    private int redisPort;

    /**
     * 단일 Topic 사용을 위한 Bean 설정
     */
    @Bean
    public ChannelTopic channelTopic() {
        return new ChannelTopic("chatroom");
    }

    /*
        유저 jwt topic
    */
    @Bean
    public ChannelTopic userJwtChannelTopic() {
        return new ChannelTopic("userJwt");
    }

    /**
     * redis pub/sub 메시지를 처리하는 listener 설정
     */
    @Bean
    public RedisMessageListenerContainer redisMessageListener(
        RedisConnectionFactory redisConnectionFactory,
        MessageListenerAdapter listenerAdapter,
        ChannelTopic channelTopic,
        MessageListenerAdapter userJwtListenerAdapter,
        ChannelTopic userJwtChannelTopic
    ) {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(redisConnectionFactory);
        container.addMessageListener(listenerAdapter, channelTopic);
        container.addMessageListener(userJwtListenerAdapter, userJwtChannelTopic);
        return container;
    }

    /**
     * 채팅 메시지를 처리하는 subscriber 설정 추가 (채팅)
     */
    @Bean
    public MessageListenerAdapter listenerAdapter(RedisSubscriber subscriber) {
        return new MessageListenerAdapter(subscriber, "sendMessage");
    }

    /*
        유저 JWT 정보 메시지를 처리하는 subscriber 설정 추가
    */
    @Bean
    public MessageListenerAdapter userJwtListenerAdapter(RedisUserJwtSubscriber subscriber) {
        return new MessageListenerAdapter(subscriber, "sendMessage");
    }

    //Lettuce connection factory
    @Bean
    public RedisConnectionFactory redisConnectionFactory() {
        LettuceConnectionFactory lettuceConnectionFactory = new LettuceConnectionFactory(host, redisPort);
        // lettuceConnectionFactory.setPassword();
        return lettuceConnectionFactory;
    }

    /**
     * 어플리케이션에서 사용할 redisTemplate 설정
     */
    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redisConnectionFactory);
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new Jackson2JsonRedisSerializer<>(String.class));
        return redisTemplate;
    }
    
}