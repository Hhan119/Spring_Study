package com.example.boot14.controller;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RestController;

import com.example.boot14.dto.ChatRoomDto;

@RestController
public class ChatController {

	 @MessageMapping("/chat.sendMessage")
	    @SendTo("/topic/public")
	    public ChatRoomDto sendMessage(ChatRoomDto chatDto) {
	        return chatDto;
	    }

	    @MessageMapping("/chat.addUser")
	    @SendTo("/topic/public")
	    public ChatRoomDto addUser(ChatRoomDto chatDto) {
//	    	String userName = SecurityContextHolder.getContext().getAuthentication().getName();
	    	chatDto.setContent(chatDto.getSender() + "님이 입장하셨습니다.");
	        return chatDto;
	    }
}
