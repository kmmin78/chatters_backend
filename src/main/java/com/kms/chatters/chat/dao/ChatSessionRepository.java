package com.kms.chatters.chat.dao;

import javax.annotation.PostConstruct;

import com.kms.chatters.chat.vo.ChatSession;

import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Repository
public class ChatSessionRepository {
    private final RedisTemplate<String, Object> redisTemplate;
    private HashOperations<String, String, ChatSession> chatSessionHashOps;
    private static final String CHAT_SESSIONS = "CHAT_SESSIONS";

    @PostConstruct
    private void init() {
        chatSessionHashOps = redisTemplate.opsForHash();
    }

    public void setSession(String session, String roomId, String username, String memberName) {
        ChatSession chatSession = ChatSession.builder()
                                             .session(session)
                                             .roomId(roomId)
                                             .username(username)
                                             .memberName(memberName)
                                             .build();
        chatSessionHashOps.put(CHAT_SESSIONS, chatSession.getSession(), chatSession);
    }

    public ChatSession getSession(String session) {
        return chatSessionHashOps.get(CHAT_SESSIONS, session);
    }

    public void deleteSession(String session) {
        chatSessionHashOps.delete(CHAT_SESSIONS, session);
    }
}
