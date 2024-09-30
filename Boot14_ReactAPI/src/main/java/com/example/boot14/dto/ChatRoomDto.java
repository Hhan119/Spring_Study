package com.example.boot14.dto;


import com.example.boot14.enums.ChatType;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ChatRoomDto {
    private Long id;
    private ChatType type;
	private String content;
	private String sender;
	
}
