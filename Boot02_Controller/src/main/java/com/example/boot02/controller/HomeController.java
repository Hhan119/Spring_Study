package com.example.boot02.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.boot02.dto.MemberDto;
/*
 * 	클라이언트의 요청을 처리할 컨트롤러를 정의하고, bean으로 만들기
 */
@Controller
public class HomeController {
	

	
	@ResponseBody
	@GetMapping("/members")
	public List<MemberDto> members() {
		List<MemberDto> members = new ArrayList<>();
		members.add(new MemberDto(1, "박서준", "잠실동"));
		members.add(new MemberDto(2, "백현우", "한남동"));
		members.add(new MemberDto(3, "홍해인", "강남동"));
		// list<memberDto> 객체를 리턴하면 [{},{},{},...] 형식의 JSON 문자열이 응답된다.
		return members;
	}
	
	@ResponseBody
	@GetMapping("/friends")
	public List<String> friends(){
		List<String> names = new ArrayList<>();
		names.add("호랑이");
		names.add("늑대");
		names.add("팬더");
		return names;
	}
	
	@ResponseBody
	@GetMapping("/member2")
	public MemberDto member2() {
		MemberDto dto = new MemberDto();
		dto.setNum(1);
		dto.setName("첼시");
		dto.setAddr("잉글랜드");
		return dto;
	}
	
	/*
	 *  @RespnseBody 어노테이션이 붙어 있는 메소드에서 Map 객체를 리턴하면,
  	 *  Map에 담긴 정보가 JSON 문자열로 변환 되어서 클라이언트에게 응답한다.
	 */
	@ResponseBody
	@GetMapping("/member")
	public Map<String, Object> member(){
		// DB에서 읽어온 회원 한명의 정보라고 가정
		Map<String, Object> map = new HashMap<>();
		map.put("num", 1);
		map.put("name", "첼시");
		map.put("isTeam", true);
		
		return map;
	}
	
	
	/*
	@GetMapping("/")
	public String index() {
		return "index";
	}
	*/
	@ResponseBody
	// 클라이언트가 "/hello" 경로로 요청을 하면 이 메소드가 실행된다.
	@GetMapping("/hello")
	public String hello() {
		// String type을 리턴하면서 메소드에 @ResponseBody 어노테이션을 붙여 놓으면
		// 여기서 리턴한 문자열이 클라이언트에게 그대로 출력된다.
		return "Nice to meet you!"; 
	}
	
	@ResponseBody
	@GetMapping("/fortune")
	public String fortune() {
		return "오늘의 운세 : 오늘은 행복한 날입니다~";
	}
}
