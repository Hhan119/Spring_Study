package com.example.boot06.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import jakarta.servlet.http.HttpSession;

@Controller
public class UserController {
	@GetMapping("/user/loginform")
	public String loginform() {
		return "user/loginform";
	}
	
	@PostMapping("/user/login")
	public String login(String id, String pwd, HttpSession session) {
		// 원래는 DB 내용을 읽어와서 로그인 처리를 해야하지만, 간단히 처리하자 
		session.setAttribute("id", id);
		// 로그인 후 최상위 경로로 리다이렉트
		return "redirect:/";
	}
	
	@GetMapping("/user/logout")
	public String logout(HttpSession session) {
		session.invalidate();
		return "redirect:/";
	}
}
