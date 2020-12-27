package com.kms.chatters.chat.vo;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ChatMessage {
    private String user;
    private String type;
    private String message;
    private String sendDate;
}
