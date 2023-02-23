package hh5.twogaether.domain.chat.entity;

import hh5.twogaether.util.BaseEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import hh5.twogaether.domain.users.entity.User;

import javax.persistence.*;

@Getter
@Setter
@Entity
@NoArgsConstructor
public class ChatMessage {
    // 메시지 타입 : 입장, 채팅
    public enum MessageType {
        ENTER, TALK
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private MessageType type; // 메시지 타입
    //    private Long roomId; // 방번호
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_id")
    private ChatRoom roomId;
    private String sender; // 메시지 보낸사람
    //    @ManyToOne
//    private User sender;
    private String message; // 메시지

//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "user_id")
//    private User user;
//    @ManyToOne
//    private User user;
//    @ManyToOne
//    private User user2;
}
