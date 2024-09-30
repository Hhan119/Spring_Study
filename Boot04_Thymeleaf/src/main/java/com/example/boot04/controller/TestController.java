package com.example.boot04.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.example.boot04.dto.MemberDto;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
public class TestController {
	@GetMapping("/sub/play")
	public String play() {
		
		// Templates/sub/play.html 템플릿을 해석한 결과로 응답해라
		return "sub/play";
	}
	
	@GetMapping("/notice")
	public String notice(Model model) {
		// DB에서 읽어온 공지사항이라고 가정
		List<String> list = new ArrayList<>();
		list.add("좋은날이에요!");
		list.add("행복할거에요!");
		list.add("건강하세요!");
		list.add("대박나세요!");
		// Thymeleaf 템플릿 엔진에서 사용할 데이터를 Model 객체에 담기
		model.addAttribute("list", list);
		// /Templates/sub/notice.html을 해석한 결과를 응답하기
		return "sub/notice";
	}
	
	@GetMapping("/member")
	public String member(Model model) {
		MemberDto dto = new MemberDto(1, "첼시", "잉글랜드");
		model.addAttribute("dto", dto);
		
		return "sub/member";
	}
	
	@GetMapping("/members")
	public String members(Model model) {
		List<MemberDto> list = new ArrayList<>();
		list.add(new MemberDto(1,"첼시","잉글랜드"));
		list.add(new MemberDto(2,"레알","스페인"));
		list.add(new MemberDto(3,"맨시티","잉글랜드"));
		model.addAttribute("list", list);
		return "sub/members";
	}
	
	@GetMapping("/shop/buy")
	public String buy(int id, int count) {
		return "sub/buy";
	}	
	
	@GetMapping("/inc")
	public String inc() {
		
		return "sub/inc";
	}
	
	@GetMapping("/unescape")
	public String unescape(Model model) {
		// 출력할 내용이 때로는 markup 형태 일때도 있다.
		String content ="<a href='https://naver.com'>naver</a>";
		model.addAttribute("content", content);
		return "sub/unescape";
	}
	
}
