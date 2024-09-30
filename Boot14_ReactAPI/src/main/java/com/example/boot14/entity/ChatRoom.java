package com.example.boot14.entity;

import java.time.LocalDateTime;
import java.util.List;

import com.example.boot14.dto.ChatRoomDto;
import com.example.boot14.enums.ChatType;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "chat_rooms")
public class ChatRoom {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "created_at", nullable = false)
	private LocalDateTime createdAt = LocalDateTime.now();

	@Enumerated(EnumType.STRING)
	@Column(name = "type", nullable = false)
	private ChatType type;

	@OneToMany(mappedBy = "room", cascade = CascadeType.ALL)
	private List<ChatParticipant> participants;

	public static ChatRoom toEntity(ChatRoomDto dto, ChatType chatType) {
		return ChatRoom.builder().
				id(dto.getId())
				.type(dto.getType())
				.createdAt(LocalDateTime.now())
				.build();
	}
}