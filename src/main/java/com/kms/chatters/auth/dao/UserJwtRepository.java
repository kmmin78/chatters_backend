package com.kms.chatters.auth.dao;

import javax.annotation.Resource;

import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Repository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Repository
public class UserJwtRepository {

    @Resource(name="redisTemplate")
    private ValueOperations<String, String> userSessionValueOps;

    public void setUserJwt(String username, String jwt) {
        userSessionValueOps.set(username, jwt);
    }

    public String getUserJwt(String username) {
        return userSessionValueOps.get(username);
    }
}
