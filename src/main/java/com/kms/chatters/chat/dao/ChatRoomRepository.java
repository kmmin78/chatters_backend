package com.kms.chatters.chat.dao;

import java.util.List;
import java.util.Optional;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import com.kms.chatters.chat.vo.ChatRoom;

import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Repository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Repository
public class ChatRoomRepository {
    
    //redis
    private static final String CHAT_ROOMS = "CHAT_ROOMS";
    private static final String USER_COUNT = "USER_COUNT";
    // private final RedisTemplate<String, Object> redisTemplate;
    //redis에서 hash 자료형을 다룰 수 있는 인터페이스. (key - hashkey - value)
    @Resource(name="redisTemplate") //RedisConfig에서 생성한 Bean을 주입받음.
    private HashOperations<String, String, ChatRoom> chatRoomHashOps;
    @Resource(name="redisTemplate")
    private ValueOperations<String, String> chatRoomValueOps;

    @PostConstruct
    private void init() {
        // 위처럼 Resource 어노테이션으로 따로 주입해주지 않으면 아래처럼 초기화 해줘야한다.
        // chatRoomHashOps = redisTemplate.opsForHash();
        // topics = new HashMap<>();

        //전체 채팅방이 개설되어 있지 않으면, 개설.
        if(chatRoomHashOps.get(CHAT_ROOMS, "all") == null){
            //방 개설
            createChatRoom("전체채팅방", "all");
        }
    }

    public List<ChatRoom> findAllRoom() {
        return chatRoomHashOps.values(CHAT_ROOMS);
    }

    public ChatRoom findRoomById(String roomId) {
        return chatRoomHashOps.get(CHAT_ROOMS, roomId);
    }

    public long getUserCount(String roomId) {
        return Long.valueOf(Optional.ofNullable(chatRoomValueOps.get(USER_COUNT + "_" + roomId)).orElse("0"));
    }

    public long increaseUserCount(String roomId) {
        return Optional.ofNullable(chatRoomValueOps.increment(USER_COUNT + "_" + roomId)).orElse(0L);
    }

    public long decreaseUserCount(String roomId) {
        return Optional.ofNullable(chatRoomValueOps.decrement(USER_COUNT + "_" + roomId)).filter(count -> count > 0).orElse(0L);
    }

    /**
     * 채팅방 생성 : 서버간 채팅방 공유를 위해 redis hash에 저장한다.
     */
    public ChatRoom createChatRoom(String name) {
        ChatRoom chatRoom = ChatRoom.create(name);
        //새로운 채팅방을 redis hash에 저장.
        chatRoomHashOps.put(CHAT_ROOMS, chatRoom.getRoomId(), chatRoom);
        return chatRoom;
    }

    //전체 채팅방용
    public ChatRoom createChatRoom(String name, String roomId) {
        ChatRoom chatRoom = ChatRoom.create(name);
        chatRoom.setRoomId(roomId);
        chatRoomHashOps.put(CHAT_ROOMS, chatRoom.getRoomId(), chatRoom);
        return chatRoom;
    }

}

// @RequiredArgsConstructor
// @Repository
// public class ChatRoomRepository {
    
//     //redis
//     private static final String CHAT_ROOMS = "CHAT_ROOMS";
//     private final RedisTemplate<String, Object> redisTemplate;
//     //redis에서 hash 자료형을 다룰 수 있는 인터페이스. (key - hashkey - value)
//     private HashOperations<String, String, ChatRoom> chatRoomHashOps;

//     //topic에 발행되는 메세지 처리할 listener를 등록할 수 있는 컨테이너 객체 - reidsConfig에서 하나로 관리함.
//     // private final RedisMessageListenerContainer redisMessageListenerContainer;
//     //구독 서비스 (레디스에겐 하나의 리스너로 취급됨.) - redisTemplate을 직접 사용하기 때문에 제거.
//     // private final RedisSubscriber redisSubscriber;

//     //채팅 대화 메세지를 발행하기 위한 redis topic 정보
//     //서버별로 채팅방에 매치되는 topic정보를 Map에 넣어 roomId로 찾을수 있도록 한다.
//     //즉, 스프링 서버 Heap에 생성될 뿐, redis에서 관리하는 건 아님.
//     // private Map<String, ChannelTopic> topics;

//     @PostConstruct
//     private void init() {
//         chatRoomHashOps = redisTemplate.opsForHash();
//         // topics = new HashMap<>();

//         //전체 채팅방이 개설되어 있지 않으면, 개설.
//         if(chatRoomHashOps.get(CHAT_ROOMS, "all") == null){
//             //방 개설
//             createChatRoom("전체채팅방", "all");
//         }
//     }

//     public List<ChatRoom> findAllRoom() {
//         return chatRoomHashOps.values(CHAT_ROOMS);
//     }

//     public ChatRoom findRoomById(String id) {
//         return chatRoomHashOps.get(CHAT_ROOMS, id);
//     }

//     /**
//      * 채팅방 생성 : 서버간 채팅방 공유를 위해 redis hash에 저장한다.
//      */
//     public ChatRoom createChatRoom(String name) {
//         ChatRoom chatRoom = ChatRoom.create(name);
//         //새로운 채팅방을 redis hash에 저장.
//         chatRoomHashOps.put(CHAT_ROOMS, chatRoom.getRoomId(), chatRoom);
//         return chatRoom;
//     }

//     //전체 채팅방용
//     public ChatRoom createChatRoom(String name, String id) {
//         ChatRoom chatRoom = ChatRoom.create(name);
//         chatRoom.setRoomId(id);
//         chatRoomHashOps.put(CHAT_ROOMS, chatRoom.getRoomId(), chatRoom);
//         return chatRoom;
//     }

//     /**
//      * 채팅방 입장 : redis에 topic을 만들고 pub/sub 통신을 하기 위해 리스너를 설정한다.
//      * 
//      * 현재 스프링서버에 해당 토픽이 리스너로 등록되어있는지 확인하는 작업.
//      * 등록되어 있지 않다면, 레디스에 리스너를 등록(구독)해서 pub/sub 통신을 가능하게 한다.
//      * 
//      * 
//      * 현재 topic은 redisConfig에서 빈으로 단일(싱글턴)객체로 관리하기 때문에 해당 로직 필요없음.
//      * 생각해보면 간단하다. redis에는 하나의 토픽만 있으면 되고, 해당 토픽으로 발행한 메세지에서 roomId를 추출해서 websocket에선
//      * 동적 topic으로 발행할 수 있음.
//      * 
//      */
//     // public void enterChatRoom(String roomId) {
//     //     ChannelTopic topic = topics.get(roomId);
//     //     //기존 topic이 없으면
//     //     if (topic == null) {
//     //         //새로운 topic 생성
//     //         topic = new ChannelTopic(roomId);
//     //         //redis 메세지 리스너(Subscriber라고 해도 됨), topic 등록
//     //         redisMessageListenerContainer.addMessageListener(redisSubscriber, topic);
//     //         topics.put(roomId, topic);
//     //     }
//     // }

//     // public ChannelTopic getTopic(String roomId) {
//     //     return topics.get(roomId);
//     // }



// }