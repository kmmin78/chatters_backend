package com.kms.chatters.chat.service;

import com.kms.chatters.chat.dao.ChatRoomRepository;
import com.kms.chatters.chat.dao.ChatSessionRepository;
import com.kms.chatters.chat.vo.ChatMessage;
import com.kms.chatters.chat.vo.ChatSession;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ChatService {
    
    private final ChatSessionRepository chatSessionRepository;
    private final ChatRoomRepository chatRoomRepository;
    private final RedisTemplate<String, Object> redisTemplate;
    private final ChannelTopic channelTopic;

    /**
     * destination정보에서 roomId 추출
     */
    public String getRoomId(String destination) {
        int lastIndex = destination.lastIndexOf('/');
        if (lastIndex != -1)
            return destination.substring(lastIndex + 1);
        else
            return "";
    }

    //메세지 보내기
    public void sendChatMessage(ChatMessage message) {
        //유저 접속 수 세팅
        message.setUserCount(
            chatRoomRepository.getUserCount(message.getRoomId())
        );
        //redis에 메세지 발행
        redisTemplate.convertAndSend(channelTopic.getTopic(), message);
    }

    //채팅 방 유저 접속수 +1
    public void increaseUserCount(String roomId) {
        chatRoomRepository.increaseUserCount(roomId);
    }

    //채팅 방 유저 접속수 -1
    public void decreaseUserCount(String roomId) {
        chatRoomRepository.decreaseUserCount(roomId);
    }

    //채팅 방 유저 접속수 가져오기
    public long getUserCount(String roomId) {
        return chatRoomRepository.getUserCount(roomId);
    }

    //세션 세팅
    public void setSession(String session, String roomId, String username, String memberName) {
        chatSessionRepository.setSession(session, roomId, username, memberName);
    }

    //세션 가져오기
    public ChatSession getSession(String session) {
        return chatSessionRepository.getSession(session);
    }

    //세션 삭제
    public void deleteSession(String session) {
        chatSessionRepository.deleteSession(session);
    }

}
