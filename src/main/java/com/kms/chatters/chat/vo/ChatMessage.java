package com.kms.chatters.chat.vo;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ChatMessage {

    public enum MessageType {
        ENTER, EXIT, MESSAGE
    }
    
    private String roomId;
    private long userCount;
    private String username;
    private String memberName;
    private MessageType type;
    // private String type;
    private String message;
    private String sendDate;

    public ChatMessage(){

    }

    @Builder
    public ChatMessage(
        String roomId,
        long userCount,
        String username, 
        String memberName, 
        MessageType type, 
        String message, 
        String sendDate
    ) {
        this.roomId = roomId;
        this.userCount = userCount;
        this.username = username;
        this.memberName = memberName;
        this.type = type;
        this.message = message;
        this.sendDate = sendDate;
    }

    
}
