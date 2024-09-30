package com.example.boot14.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.boot14.dto.MemberDto;
import com.example.boot14.service.MemberService;

@RestController
//@RequestMapping("/v1")
public class MemeberController {
	@Autowired 
	private MemberService service;
	
	@GetMapping("/members")
	public List<MemberDto> members() {
		return service.selectListMember();
	}
	/*
	 * 	axios.post("/members/", object type)로 전송 했기 때문에,
	 * 	request의 body에는 json 문자열이 들어있다.
	 * 	json 문자열을 java 객체로 받기 위해서는 @RequestBody 어노테이션이 필요하다.
	 * 
	 * 	{"name":"입력한 이름", "addr":"입력한 주소"}
	 * 	입력한 이름은 MemberDto의 name 이라는 필드에
	 * 	입력한 주고는 MemberDto의 addr 이라는 필드에 자동으로 담긴다.
	 */
	@PostMapping("/members")
	public MemberDto save(@RequestBody MemberDto dto) {
		System.out.println(dto);
		// 서비스가 리턴해주는 MemberDto에는 추가된 회원의 번호도 들어있다.
		return service.addMember(dto);
	}
	
	@DeleteMapping("/members/{num}")
	public Map<String, Object> delete(@PathVariable("num") int num) {
		service.deleteMember(num);
		
		Map<String, Object> map = new HashMap<>();
		map.put("isSuccess", true);
		return map;
	}
	
	@GetMapping("/members/{num}")
	public MemberDto getData(@PathVariable("num") int num) {
		return service.selectOneMember(num);
	}
	
	// 리액트 state에 num이 들어있어서 컨트롤러에서 받아올 필요가 없다.
	@PutMapping("/members/{num}")
	public MemberDto update(@RequestBody MemberDto dto) {
		System.out.println(dto);
		service.updateMember(dto);
		return dto;
	}
}
