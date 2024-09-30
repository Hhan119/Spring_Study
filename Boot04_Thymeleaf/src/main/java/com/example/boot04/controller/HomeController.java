package com.example.boot04.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;


@Controller
public class HomeController {

	@GetMapping("/")
	public String home(Model model) {
		// Model 객체에 "personToday"라는 키값으로 String type 담기
		model.addAttribute("personToday", "아이언맨");
		
		// GET 방식 파라미터 출력 연습을 위해서 Model 객체에 미리 정보를 담아두기
		model.addAttribute("id", 99);
		model.addAttribute("count", 3);
		
		
		// 여기서 리턴한 문자열 앞에는 /templates/가 붙고, 뒤에는 html이 붙어서 /templates/home.html를 가리킨다. 
		return "home";
	}
}
