package com.kms.chatters.chat.redis;

import com.kms.chatters.chat.vo.ChatMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RedisPublisher {
    
    private final RedisTemplate<String, Object> redisTemplate;

    public void publish(ChannelTopic topic, ChatMessage message){
        redisTemplate.convertAndSend(topic.getTopic(), message);
    }
}
