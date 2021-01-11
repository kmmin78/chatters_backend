package com.kms.chatters.chat.vo;

import java.io.Serializable;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChatSession implements Serializable {
    
    private static final long serialVersionUID = 792728512452747551L;
    
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
