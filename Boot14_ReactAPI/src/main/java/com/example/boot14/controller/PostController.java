package com.example.boot14.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.boot14.dto.PostDto;
import com.example.boot14.service.PostService;

@RestController
//@RequestMapping("/v1")
public class PostController {
	// dao 객체를 주입 받기, 주입시 daoimpl에 repository or mapper 어노테이션 붙여줘야 한다.
	// 필요한 서비스 객체를 interface type으로 DI받는다.(서비스가 dao 객체에 의존해서 사용됨)
	@Autowired
	private PostService service;
	
	@ResponseBody
	@GetMapping("/posts")
	public List<PostDto> getList(){
		List<PostDto> list = service.selectAll();
		return list;
	}
	
	
	/*
	@ResponseBody
	@GetMapping("/posts/{id}")
	public PostDto getData(@PathVariable("id") int id, Model model) {
		model.addAttribute("id", id);
		PostDto dto = dao.getData(id);
		return dto;
	}
	*/
	@GetMapping("/posts/{id}")
	public PostDto getData(@PathVariable("id") int id, Model model) {
		// 서비스를 이용해서 글하나의 정보를 얻어와서 리턴해준다.
		return service.getContent(id);
	}
	/*
	@ResponseBody
	@PostMapping("/posts")
	public PostDto insert(String title, String author) {
		// @Builder의 기능을 이용해서 PostDto 객체에 데이터를 담으면서 객체의 참조값 얻어내기
		PostDto dto =PostDto.builder().title(title).author(author).build();
		// dao를 이용해서 dto에 저장된 정보를 DB에 저장
		dao.insert(dto);
		// 현재 추가된 정보를 리턴하기
		return dto;
	}
	*/
	@PostMapping("/posts")
	public PostDto insert(@RequestBody PostDto dto) {
		// 서비스를 이용해서 글을 저장하고, 리턴해주는 PostDto를 컨트롤러에서 리턴해준다.
		return service.addContent(dto);
	}
	/*
	@ResponseBody
	@PostMapping("/posts")
	// title과 author가 추출되어서 PostDto 객체에 담긴체로 전달된다.
	public PostDto insert(PostDto dto) {
		// 글번호를 미리 얻어낸다.
		int id = dao.getSequence();
		// dto에 글번호를 담는다.
		dto.setId(id);
		// DB에 저장한다.
		dao.insert(dto);
	return dto;
	}
	*/
	
	@PutMapping("/posts/{id}")
	public PostDto update(@RequestBody PostDto dto) {
		//PostDto에 경로 변수로 넘어오는 수정할 글번호도 담아서 
		//dto.setId(id);
		// 서비스를 이용해서 글을 수정한다.
		service.updateContent(dto);
		// 수정된 데이터를 리턴해준다.
		return dto;
	}
	
	/*
	@ResponseBody
	@PutMapping("/posts/{id}")
	public PostDto update(@PathVariable("id") int id, PostDto dto) {
		//PostDto에 경로 변수로 넘어오는 수정할 글번호도 담아서 
		dto.setId(id);
		// 수정 반영하고
		dao.update(dto);
		// 수정된 데이터를 리턴해준다.
		return dto;
	}
	*/
	
	@DeleteMapping("/posts/{id}")
	public PostDto delete(@PathVariable("id") int id) {
		// 서비스를 이용해서 글을 삭제하고 리턴되는 글정보를 리턴한다.
		return service.removeContent(id);
	}
	
	/*
	@ResponseBody
	@DeleteMapping("/posts/{id}")
	public PostDto delete(@PathVariable("id") int id) {
		// 삭제하기 전에 삭제할 글 정보를 미리 담아두고
		PostDto dto = dao.getData(id);
		// DB에서 삭제
		dao.delete(id);
		// 삭제된 글정보를 리턴
		return dto;
	}
	*/
	
}
