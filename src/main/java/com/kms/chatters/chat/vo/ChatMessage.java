package com.kms.chatters.chat.vo;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ChatMessage {
    private String username;
    private String memberName;
    private String type;
    private String message;
    private String sendDate;
}
