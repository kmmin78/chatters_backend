package com.kms.chatters.chat.handler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.SubscribableChannel;
import org.springframework.web.socket.messaging.SubProtocolWebSocketHandler;

public class SubProtocolWebSocketHandlerImpl extends SubProtocolWebSocketHandler {

    @Autowired
    private MessageChannel clientInboundChannel;
    @Autowired
    private SubscribableChannel clientOutboundChannel;

	public SubProtocolWebSocketHandlerImpl(MessageChannel clientInboundChannel,
			SubscribableChannel clientOutboundChannel) {
		super(clientInboundChannel, clientOutboundChannel);
		//TODO Auto-generated constructor stub
	}
    
    
}
