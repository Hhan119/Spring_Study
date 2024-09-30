package com.example.boot13.dto;

import java.text.SimpleDateFormat;
import java.util.Locale;

import com.example.boot13.entity.Cook;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class CookDto {
	private Long num;
	private String name;
	private String cookmake;
	private String regdate;

	// Entity를 DTO로 변환
	public static CookDto toDto(Cook coEntity) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd E a HH:mm:ss", Locale.KOREA);
		String str = format.format(coEntity.getRegdate());
		
		return CookDto.builder()
				.num(coEntity.getNum())
				.name(coEntity.getName())
				.cookmake(coEntity.getCookmake())
				.regdate(str)
				.build();
	}
}

