package com.example.boot08.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class HomeController {

	@GetMapping("/")
	public String home() {
		
		// templates/home.html 타임리프 페이지를 응답
		return "home";
	}
	
	@ResponseBody
	@GetMapping("/play")
	public String play() {
		
		// templates/play.html 타임리프 페이지를 응답
		return "let`s play!";
	}
	
	@ResponseBody
	@GetMapping("/study")
	public String study() {
		
		// templates/study.html 타임리프 페이지를 응답
		return "let`s study !";
	}
}
