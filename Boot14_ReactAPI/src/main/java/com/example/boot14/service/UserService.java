package com.example.boot14.service;


import com.example.boot14.dto.OAuthToken;
import com.example.boot14.dto.UserDto;
import com.example.boot14.entity.User;

public interface UserService {
    public User KakaoFindId(String username);
    public String KakaoSignUp(OAuthToken kakaoToken)  ;
    public String KakaogetAccessToken(String code);
    public String kakaoLogout(OAuthToken oAuthToken, Long kakaoId);
    public String GoogleAccessToken(String decode);
    public String GoogleSignUp(OAuthToken googleToken)  ;
    public String GoogleLogout(OAuthToken oAuthToken);
    public void addUser(UserDto dto);
    public UserDto getInfo();
    public void updateUser(UserDto dto);
    public void updatePassword(UserDto dto);
    public boolean canUse(String userName);
}