package com.example.boot14.dto;

import org.apache.ibatis.type.Alias;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@Alias("PostDto")
public class PostDto {

	private int id;
	private String title;
	private String author;
}
