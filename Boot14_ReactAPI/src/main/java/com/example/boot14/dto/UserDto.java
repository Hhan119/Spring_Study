package com.example.boot14.dto;

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
	

//@AllArgsConstructor
//@NoArgsConstructor
//@Builder
//@Data
//public class UserDto {
//	private int id;
//	
//	private String username;
//	private String password;
//	
//	private String nickname;
//			  
//	private int age;
//	private String name;
//	private String gender;
//	private String phone_num;
//	private String email; // [note: "인증 받으면 email 로그인 사용 가능"]
//
//	private String profile_pics;
//	private String profile_msg;
//	
//	private String cur_location;
//	private String verification_status; // [note: "인증 상태"]
//	private String account_status; // [note: "관리자의 조치"]
//	private String social_links;
//			  
//	private String role; // [note: "user / manager / admin"]
//
//	private float ratings; // 지표 설정 
//
//	private String last_login; // 몇분전 접속  
//	
//	private String created_at; 
//	private String updated_at; 
//	private String deleted_at; //  [note:"soft delete 지원?"]
//	
//	public static UserDto toDto(User entity) {
//		
//	    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd E a hh:mm:ss", Locale.KOREA);
//	    String enCreated_at =  dateFormat.format(entity.getCreated_at());
//	    String enUpdated_at =  dateFormat.format(entity.getUpdated_at());
//	    String enDeleted_at =  dateFormat.format(entity.getDeleted_at());
//	    
//	    return UserDto.builder()
//	            .id(entity.getId())
//	            .username(entity.getUsername())
//	            .password(entity.getPassword())
//	            .nickname(entity.getNickname())
//	            .age(entity.getAge())
//	            .name(entity.getName())
//	            .gender(entity.getGender())
//	            .phone_num(entity.getPhone_num())
//	            .email(entity.getEmail())
//	            .profile_pics(entity.getProfile_pics())
//	            .profile_msg(entity.getProfile_msg())
//	            .cur_location(entity.getCur_location())
//	            .verification_status(entity.getVerification_status())
//	            .account_status(entity.getAccount_status())
//	            .social_links(entity.getSocial_links())
//	            .role(entity.getRole())
//	            .ratings(entity.getRatings())
//	            .last_login(entity.getLast_login())
//	            .created_at(entity.getCreated_at() != null ? entity.getCreated_at().toString() : null)
//	            .updated_at(entity.getUpdated_at() != null ? entity.getUpdated_at().toString() : null)
//	            .deleted_at(entity.getDeleted_at() != null ? entity.getDeleted_at().toString() : null)
//	            .build();
//	}
}
