package com.example.boot14.entity;

import java.time.LocalDateTime;


import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;

@Builder
@Entity
@Getter
@Table(name = "chat_participants")
public class ChatParticipant {

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "room_id", nullable = false)
    private ChatRoom room;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
	
public static ChatParticipant toEntity(ChatRoom room, User user) {
    return ChatParticipant.builder()
        .room(room) 
        .user(user)  
        .build();
	}
}