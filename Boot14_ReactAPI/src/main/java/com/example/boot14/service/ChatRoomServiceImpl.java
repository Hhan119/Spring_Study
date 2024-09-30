package com.example.boot14.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.boot14.dto.ChatRoomDto;
import com.example.boot14.entity.ChatRoom;
import com.example.boot14.entity.User;
import com.example.boot14.enums.ChatType;
import com.example.boot14.repository.ChatParticipantRepository;
import com.example.boot14.repository.ChatRoomRepository;
import com.example.boot14.repository.UserRepository;

@Service
public class ChatRoomServiceImpl implements ChatRoomService {
	private final ChatRoomRepository chatRoomRepository;
	private final UserRepository userRepository;
	private final ChatParticipantRepository chatparticipantRepository;


	public ChatRoomServiceImpl(ChatRoomRepository chatRoomRepository, ChatParticipantRepository chatparticipantRepository,
			UserRepository userRepository) {
		this.chatRoomRepository = chatRoomRepository;
		this.userRepository = userRepository;
		this.chatparticipantRepository =chatparticipantRepository;
	}

	@Override
	public ChatRoom findOrCreateRoom(List<Long> userIds, ChatRoomDto dto) {
		if (dto.getType() == ChatType.ONE_ON_ONE && userIds.size() == 2) {
			return findOrCreateOneOnOneRoom(userIds, dto);
		}
		return findOrCreateGroupRoom(userIds, dto);
	}

	@Override
	public ChatRoom findOrCreateOneOnOneRoom(List<Long> userIds, ChatRoomDto dto) {
		Long userId1 = userIds.get(0);
		Long userId2 = userIds.get(1);

		Optional<ChatRoom> existingRoom = chatRoomRepository.findOneOnOneRoom(userId1, userId2);
		if (existingRoom.isPresent()) {
			return existingRoom.get();
		} else {
			ChatRoom newRoom = ChatRoom.toEntity(dto, ChatType.ONE_ON_ONE);
			chatRoomRepository.save(newRoom);

			// User 객체가 필요한 경우 UserRepository에서 가져와야 합니다.
			User user1 = userRepository.findById(userId1);
			User user2 = userRepository.findById(userId2);

//			chatparticipantRepository.save(newRoom, user1);
//			chatparticipantRepository.save(newRoom, user2);

			return newRoom;
		}
	}

	@Override
	public ChatRoom findOrCreateGroupRoom(List<Long> userIds, ChatRoomDto dto) {
		Optional<ChatRoom> existingRoom = chatRoomRepository.findGroupRoomByParticipants(userIds,
				(long) userIds.size());
		return existingRoom.orElseGet(() -> {
			ChatRoom newRoom = ChatRoom.toEntity(dto, ChatType.GROUP);
			chatRoomRepository.save(newRoom);

			for (Long userId : userIds) {
				User user = userRepository.findById(userId);
//				chatparticipantRepository.save(newRoom, user);
			}
			return newRoom;
		});
	}

}
