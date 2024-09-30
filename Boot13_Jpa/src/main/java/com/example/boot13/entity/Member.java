package com.example.boot13.entity;

import com.example.boot13.dto.MemberDto;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Setter
@Getter // Entity는 Getter 만 제공한다.(한번 만들어진 객체는 ReadOnly로 사용되도록 한다)
@Entity(name="MEMBER_INFO")
public class Member {
	@Id // PRIMARY kEY (대표키)로 설정하겠다
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long num;
	private String name;
	private String addr;

	// dto를 entity로 변환하는 static 메소드
	public static Member toEntity(MemberDto dto) {
		return Member.builder()
				.num(dto.getNum())
				.name(dto.getName())
				.addr(dto.getAddr())
				.build();
		
	}
	
}
