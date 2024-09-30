package com.example.posts.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.posts.dao.PostDao;
import com.example.posts.dto.PostDto;

@Controller
public class PostController {
	@Autowired
	private PostDao dao;
	
	// GET 방식 
	@ResponseBody
	@GetMapping("/posts")
	public List<PostDto> selectList(Model model){
		List<PostDto> list = dao.selectList();
		model.addAttribute("list", list);
		return dao.selectList();
	}
	
	@PostMapping("/posts")
	public String insert(@RequestBody PostDto dto) {
		dto.setTitle("제목");
		dto.setAuthor("작성");
		dao.insert(dto);
		return insert(dto);
	}
	// GET 방식/{id}
	
	// POST 방식 
	
	
}
