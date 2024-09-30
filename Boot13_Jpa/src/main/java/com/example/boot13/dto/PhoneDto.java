package com.example.boot13.dto;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Locale;

import com.example.boot13.entity.Phone;

import jakarta.persistence.Column;
import jakarta.persistence.PrePersist;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class PhoneDto {
	private long id;
	private String company;
	private String name;
	private int price;	
	private String regdate;

	public static PhoneDto toDto(Phone phone) {
		 // Date type을 이용해서 원하는 문자열 형식을 얻어내기 위한 객체
		 SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd E a HH:mm:ss", Locale.KOREA);
		 // 객체를 이용해서 원하는 문자열을 얻어낸다.
		 // 2024.08.01 목 오후 06:10:30 이런 형식의 문자열
		 String str = format.format(phone.getRegdate());
		 
		return PhoneDto.builder()
				.id(phone.getId())
				.name(phone.getName())
				.company(phone.getCompany())
				.price(phone.getPrice())
				.regdate(str)
				.build();
	}
}
