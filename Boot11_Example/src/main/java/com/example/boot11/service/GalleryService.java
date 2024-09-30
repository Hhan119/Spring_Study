package com.example.boot11.service;


import org.springframework.ui.Model;
import org.springframework.web.multipart.MultipartFile;

import com.example.boot11.dto.GalleryDto;

public interface GalleryService {
	// 갤러리에 사진 추가
	public void saveImage(GalleryDto dto);
	// pageNum에 해당하는 갤러리 목록을 Model 객체에 담기
	public void getListImage(GalleryDto dto, Model model);
	public void selectPage(Model model, int pageNum);
	// num에 해당하는 갤러리 정보 삭제
	public void deleteImage(int num);
	// Model 객체에 갤러리정보 하나 담기 
	public void getDataImage(int num, Model model);
}
