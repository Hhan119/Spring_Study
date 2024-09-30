package com.example.boot13.entity;

import java.util.Date;

import com.example.boot13.dto.CookDto;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Entity(name="COOK_INFO")
@Builder
@Getter
public class Cook {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long num;
	private String name;
	private String cookmake;
	@Column(nullable = false)
	private Date regdate;
	
	// dto를 엔티티로 변환
	public static Cook toEntity(CookDto dto) {
		return Cook.builder()
				.num(dto.getNum())
				.name(dto.getName())
				.cookmake(dto.getCookmake())
				.build();
	}
	
	// Entity를 영속화 하기 직전에 뭔가 작업할게 있으면, @PrePersist 어노테이션을 활용 하면된다.
		@PrePersist
		public void onPersist() {
			// 오라클에서 데이터를 넣을때 SYSDATE 함수를 이용해서 넣는 효과를 낸다.
			regdate = new Date();
		}
}
