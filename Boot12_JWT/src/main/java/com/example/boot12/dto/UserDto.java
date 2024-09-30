package com.example.boot12.dto;

import org.springframework.web.multipart.MultipartFile;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
/*
CREATE TABLE user_tbl
 (id NUMBER PRIMARY KEY,
 	userName VARCHAR2(20) UNIQUE,
 	password VARCHAR2(100) NOT NULL,
  	email VARCHAR2(100),
  	role VARCHAR2(20) NOT NULL,
  	profile VARCHAR2(100),
  	regdate DATE
 );

CREATE SEQUENCE user_seq;
*/

@Builder // .actin().actio2() 형태로 객체를 만들 수 있게 해준다.
@AllArgsConstructor // 모든 인자를 전달받는 생성자
@NoArgsConstructor // default 생성자
@Data // setter. getter 메소드 등을 생성해준다.
public class UserDto {
	// 숫자로된 아이디는 PK
	private int id;
	// 사용자명은 중복된 데이터가 들어가지 않도록 UNIQUE KEY 를 설정 해야한다.
	private String userName;
	private String password;
	private String newPassword;
	private String email;
	// Authority 정보를 저장할 칼럼 ADMIN | STAFF | USER 형식이다.
	private String role;
	private String profile;
	private String regdate;
	
	// 프로필 이미지 파일 업로드 처리를 하기 위한 필드
	// <input type="file" name="image"> 임으로 필드명이 image이다. 
	private MultipartFile image; 
}
