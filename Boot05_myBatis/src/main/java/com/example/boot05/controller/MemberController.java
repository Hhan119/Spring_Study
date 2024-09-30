package com.example.boot05.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import com.example.boot05.dao.MemberDao;
import com.example.boot05.dto.MemberDto;

@Controller
public class MemberController {
	// spring bean container로부터 MemberDao type 주입(DI) 받기
	@Autowired
	private MemberDao dao;
	
	@GetMapping("/member/list")
	public String list(Model model) {
		// DB에서 회원목록(List<MemberDto>)을 얻어와서 Model 객체에 담고
		List<MemberDto> list = dao.selectList();
		model.addAttribute("list", list);
		
		// 타임리프 view 페이지를 이용해서 응답하기
		return "member/list";
	}

	@GetMapping("/member/insertform")
	public String insertForm() {
		
		return "member/insertform";
	}
	
	@PostMapping("/member/insert")
	public String insert(MemberDto dto) {
		dao.insert(dto);
		return "member/insert";
	}
	
	@GetMapping("/member/delete")
	public String delete(int num) {
		// MemberDao 객체를 이용해서 삭제하고
		dao.delete(num);
		// 회원 목록 보기로 리다일렉트 이용하나는 응답하기
		return "redirect:/member/list";
	}
	
	@GetMapping("/member/updateform")
	public String updateform(int num, Model model) {  // 수정할 회원의 번호가 자동으로 추출되어서 num매개변수에 전달된다.
		// 수정할 회원의 번호를 이용해서 회원정보를 얻어온다.
		MemberDto dto = dao.getData(num);
		// 얻어온 회원 정보를 Model 객체에 담는다.
		model.addAttribute("dto", dto);
		// 회원정보를 수정폼을 응답한다.
		return "member/updateform";
	}
	
	@PostMapping("member/update")
	public String getData(MemberDto dto) { // 수정할 회원의 번호가 자동으로 추출되어서 num매개변수에 전달된다.
		dao.update(dto);
		return "member/update";
	}
}
