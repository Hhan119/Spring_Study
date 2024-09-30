package com.example.boot14.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown=true)
public class GoogleProfile {
//    private String id;
//    private String email;
//    private String verified_email;
//    private String name;
//    private String given_name;
//    private String family_name;
//    private String picture;
//    private String locale;
	   // 구글 프로필에서의 사용자 ID
    private String sub;

    // 사용자 이름
    private String name;

    // 사용자 이름 (주어진 이름)
    @JsonProperty("given_name")
    private String givenName;

    // 사용자 성
    @JsonProperty("family_name")
    private String familyName;

    // 프로필 사진 URL
    private String picture;

    // 이메일
    private String email;

    // 이메일 인증 여부
    @JsonProperty("email_verified")
    private boolean emailVerified;

    // 사용자 지역
    private String locale;

    // 기본 생성자
    public GoogleProfile() {
    }

}
