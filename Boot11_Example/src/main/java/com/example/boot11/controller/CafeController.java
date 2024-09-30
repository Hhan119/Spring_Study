package com.example.boot11.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.boot11.dto.CafeCommentDto;
import com.example.boot11.dto.CafeDto;
import com.example.boot11.service.CafeService;

@Controller
public class CafeController {
	@Autowired 
	private CafeService service;
	
	@GetMapping("/cafe/list")
	public String list(CafeDto dto, Model model) {
		// PageNum or keyword가 파라미터로 전달된다면, dto에 들어있다. 
		service.getList(dto, model);
		return "cafe/list";
	}
	
	// cafeinsertform에서 smarteditor 기능 중 사진 첨부는 jsp로 되어 있어서(Spring에서 작동 안함) 컨트롤러에서 작동하게 한다.
	@GetMapping("/cafe/insertform")
	public String insertform() {
		
		return "cafe/insertform";
	}
	
	@PostMapping("/cafe/insert")
	public String insert(CafeDto dto) {
		service.saveContent(dto);
		return "cafe/insert";
		
	}
	
	@GetMapping("/cafe/detail")
	public String detail(CafeDto dto, Model model) {
		service.getDetail(dto, model);
		return "cafe/detail";
		
	}
	@GetMapping("/cafe/delete")
	public String delete(int num) {
		service.deleteContent(num);
		return "redirect:/cafe/list";
	}
	@GetMapping("/cafe/updateform")
	public String updateform(Model model, int num) {
		service.getData(model, num);
		return "cafe/updateform";
	}
	
	@PostMapping("/cafe/update")
	public String update(CafeDto dto, Model model) {
		// 서비스 객체를 이용해서 수정 반영하고
		service.updateContent(dto);
		// 해당 글 자세히 보기로 리다이렉트 이동(GET 방식 parameter로 글번호도 전달해야 한다.)
		return "redirect:/cafe/detail?num="+dto.getNum();
	}
	
	// CafeCommentDto 에는 ref_group, target_id, content 3개의 정보가 들어있다. 
	// 대댓글인 경우에는 comment_group 번호도 같이 넘어온다.
	@PostMapping("/cafe/comment_insert")
	public String update(CafeCommentDto dto) {
		// 댓글 저장
		service.saveComment(dto);
		// 해당 글 자세히 보기로 다시 리다이렉트 시킨다.
		return "redirect:/cafe/detail?num="+dto.getRef_group();
	}
	
	// Map 객체를 리턴하면 json 문자열이 응답되도록 @ResponseBody 어노테이션을 추가로 붙여준다.
	@ResponseBody 
	@GetMapping("/cafe/comment_delete")
	public Map<String, Object> commentDelete(int num){
		// num은 Get 방식 파라미터로 전달되는 삭제 할 댓글의 번호
		service.deleteComment(num);
		
		Map<String, Object> map = new HashMap<>();
		map.put("isSuccess", true);
		return map;
	}
	
	@ResponseBody
	@PostMapping("/cafe/comment_update")
	public Map<String, Object> commentUpdate(CafeCommentDto dto) {
		service.updateComment(dto);
		// 수정된 글의 정보를 json으로 응답
		Map<String, Object> map =new HashMap<>();
		map.put("isSuccess", true);
		map.put("num", dto.getNum());
		map.put("content", dto.getContent());
		return map;
	}
	
	@GetMapping("/cafe/comment_list")
	public String commentList(CafeCommentDto dto, Model model) {
		// CafeCommentDto에는 pageNum, ref_group이 들어있다.(GET 방식 파라미터)
		service.getCommentList(model, dto);
		
		try {
			// 테스트를 위해 임의로 스레드를 3초 정도 지연시키기
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		// templates/cafe/comment_list.html에서 댓글이 들어 있는 여러개의 li를 응답할 예정
		return "cafe/comment_list";
	}
}
