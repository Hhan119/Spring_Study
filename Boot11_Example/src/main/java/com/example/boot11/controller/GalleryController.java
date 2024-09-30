package com.example.boot11.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.boot11.dto.GalleryDto;
import com.example.boot11.service.GalleryService;
import com.example.boot11.service.GalleryServiceImpl;

import lombok.extern.slf4j.Slf4j;
@Slf4j
@Controller
public class GalleryController {
	@Value("${file.location}")
	private String fileLocation;
	
	@Autowired
	private GalleryService service;
	/*
	@GetMapping("/gallery/list")
	public String list(Model model, GalleryDto dto) {
		service.getListImage(dto, model);
		return "gallery/list";
	}*/
	
	@GetMapping("/gallery/list")
	public String list(Model model, @RequestParam(defaultValue = "1") int pageNum) {
		/*
		 * 	서비스에 Model 객체의 pageNum을 전달해서 Model에 pageNum에 해당하는 글 목록이 담기도록 한다.
		 * 	Model에 담긴 내용을 view page(Thymeleaf 템플릿페이지)에서 사용할 수 있다. 
		 */
		service.selectPage(model, pageNum);
		return "gallery/list";
	}
	
	
	@GetMapping("/gallery/uploadform")
	public String uploadForm(Model model) {
		GalleryDto dto = new GalleryDto();
		model.addAttribute("dto", dto);
		return "gallery/uploadform";
	}
	
	@PostMapping("/gallery/upload")
	public String upload(GalleryDto dto, Model model) {
			service.saveImage(dto);
			model.addAttribute("dto", dto);
		return "gallery/upload";
	}
	
	@GetMapping("/gallery/delete")
	public String delete(int num) {
		service.deleteImage(num);
		return "redirect:/gallery/list";
	}
	
	@GetMapping("/gallery/detail")
	public String getDetail(Model model, int num) {
		// num에는 gallery의 pk가 들어있다. 
		service.getDataImage(num, model);
		return "gallery/detail";
	}
}
