package com.kms.chatters.auth.service;

import com.kms.chatters.auth.dao.UserJwtRepository;
import com.kms.chatters.auth.vo.UserJwtMessage;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class UserService {

    private final UserJwtRepository userJwtRepository; 
    private final RedisTemplate<String, Object> redisTemplate;
    private final ChannelTopic userJwtChannelTopic;

    public void sendMessage(UserJwtMessage message) {
        //redis에 메세지 발행
        redisTemplate.convertAndSend(userJwtChannelTopic.getTopic(), message);
    }

    /*
        인증, 인가 시에는 따로 세션이나 DB를 사용하지 않고 jwt에서 추출한다.
        동시로그인 시 기존 로그인 유저에게 publish 해서 로그아웃 시키기 위해 
        redis에 key : username, value : jwt로 저장한다.
    */
    public void setUserJwt(String username, String jwt) {
        userJwtRepository.setUserJwt(username, jwt);
    }

    public String getUserJwt(String username) {
        return userJwtRepository.getUserJwt(username);
    }
    
}
