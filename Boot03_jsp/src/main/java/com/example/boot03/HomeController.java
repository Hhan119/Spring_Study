package com.example.boot03;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.example.boot03.dto.MemberDto;

import jakarta.security.auth.message.callback.PrivateKeyCallback.Request;
import jakarta.servlet.http.HttpServletRequest;

@Controller
public class HomeController {
	
	// jsp 페이지에게 응답에 필요한 데이터를 전달하는 방법
	// HttpServletRequest 객체에 .setAttribute() 메소드를 이용새서 담는다.
	@GetMapping("/")
	public String home(HttpServletRequest request) {
		
		// 공지사항을 DB에서 가져왔다고 가정
		List<String> noticeList = new ArrayList<>();
		noticeList.add("장마가 시작 되었습니다.");
		noticeList.add("spring boot 수업이 시작 되었습니다.");
		noticeList.add("우산을 챙기고 다니세요!");
		noticeList.add("집에 가요!");
		// request scope에 "noticeList"라는 키값으로 List<String> type을 담기
		request.setAttribute("noticeList", noticeList);
		// "/WEB-INF/views/home.jsp" 페이지로 응답을 위임하기(forward 이동)
		return "home";
		
	}
	
	// jsp 페이지에게 응답에 필요한 데이터를 전달하는 방법 
	// model 객체에 .addAttribute() 메소드를 이용해서 담는다.
	@GetMapping("/fortune")
	public String fortune(Model model) {
		// 오늘의 운세를 DB에서 읽어왔따고 가정하자
		String fortuneToday ="로또의 당첨되는 날입니다!";
		// "fortuneToday" 라는 키값으로 String type을 담고
		model.addAttribute("fortuneToday", fortuneToday);
		// "/WEB-INF/views/home.jsp" 페이지로 응답을 위임하기(forward 이동)
		return "fortune";
	}
	
	@GetMapping("/member")
	public String member(Model model) {
		MemberDto dto = new MemberDto();
		dto.setNum(1);
		dto.setName("첼시");
		dto.setAddr("잉글랜드");
		model.addAttribute("dto", dto);
		return "member";
	}
	
}
