package com.kms.chatters.chat.config;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.redis.connection.RedisServer;

@Profile("local")
@Configuration
public class EmbeddedRedisConfig {
    @Value("${spring.redis.host")
    private String host;
    @Value("${spring.redis.port}")
    private int redisPort;

    // private RedisServer redisServer;
    
    @PostConstruct
    public void redisServer() {
        // redisServer = new RedisServer(redisPort);
    }
    
}
