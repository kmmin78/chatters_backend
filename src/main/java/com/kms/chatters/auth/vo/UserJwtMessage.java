package com.kms.chatters.auth.vo;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserJwtMessage {
    String username;
    String jwt;

    public UserJwtMessage() {
        
    }

    @Builder
    public UserJwtMessage(
        String username,
        String jwt
    ) {
        this.username = username;
        this.jwt = jwt;
    }
}
