package com.example.boot14.service;

import java.util.List;

import com.example.boot14.dto.ChatRoomDto;
import com.example.boot14.entity.ChatRoom;



public interface ChatRoomService {
	public ChatRoom findOrCreateRoom(List<Long> userIds, ChatRoomDto dto);
	public ChatRoom findOrCreateOneOnOneRoom(List<Long> userIds, ChatRoomDto dto);
	public  ChatRoom findOrCreateGroupRoom(List<Long> userIds, ChatRoomDto dto);
	
}
