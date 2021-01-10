package com.kms.chatters.chat.vo;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChatSession {
    String session;
    String roomId;
    String username;
    String memberName;

    @Builder
    public ChatSession(
        String session, 
        String roomId,
        String username,
        String memberName
    ) {
        this.session = session;
        this.roomId = roomId;
        this.username = username;
        this.memberName = memberName;
    }
}
