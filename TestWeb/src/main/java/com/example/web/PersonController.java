package com.example.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import ch.qos.logback.core.model.Model;
// 클라이언트의 요청을 처리할 컨트롤러 생성
// 컨트롤러 역할을 할 수 있는 bean으로 생성한다.
@Controller
public class PersonController {
	// 특정 경로 요청을 처리할 메소드 생성
	
	// 리턴하는 데이터를 클라이언트에게 출력하도록 하는 어노테이션
	@ResponseBody 
	// GET 방식 /person 요청이 왔을 때 이 메소드가 호출되도록 설정 
	@GetMapping("/person")
	public String index() {
		return "오늘의 주요 팀은 첼시입니다. ";
	}

}
