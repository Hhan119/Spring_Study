package com.example.boot14.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.boot14.entity.User;

public interface UserRepository extends JpaRepository<User, Integer>{
	public User findByUserName(String username);

	public User findById(Long kakaoId);

	public void save(Long kakaoId);


}// KakaoId가 String 타입일 경우
