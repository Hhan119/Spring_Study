package com.example.boot11.controller;

import java.util.Arrays;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {
	// 최상위 경로 요청에 대해서 응답할 컨트롤러 메소드 
	@GetMapping("/")
	// 컨트롤러 메소드에서 필요한 객체가 있으면 매개변수에 선언한다.
	public String home(Model model) {
		// 응답에 필요한 데이터는 HttpServletRequest에 담거나 혹은 Model 객체에 담으면 view engine에서 사용 가능한다.
		// DB에서 읽어온 공지 사항이라고 가정
		List<String> notice = Arrays.asList("첼시", "아스날", "바르셀로나");
		// 어떤 key 값으로 어떤 type 데이터를 담았는지 기억을 하고, 템플릿 페이지를 생성 해야한다.
		model.addAttribute("notice", notice);
		
		// /templates/home.html 타임리프 페이지로 응답
		return "home";
	}
}
