package com.example.boot07.dto;

import org.springframework.web.multipart.MultipartFile;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

// 생성자의 인자로 모든 필드의 값을 전달 받는 생성자(Builder가 동작하기 위해서 필요)
@AllArgsConstructor
// default 생성자
@NoArgsConstructor
@Builder
// @Setter + @Getter + 몇가지 옵션을 포함 
@Data
public class FileDto {
	private int num;
	private String writer;
	private String title;
	private String orgFileName;
	private String saveFileName;
	private long fileSize;
	private MultipartFile myFile;
	
}
