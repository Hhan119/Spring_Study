package com.example.boot14.service;

import java.util.List;
import java.util.Map;

import com.example.boot14.dto.GalleryDto;


public interface GalleryService {
	// pageNum에 해당하는 gallery 페이지 정보를 Map에 담아서 리턴하는 메소드
	public Map<String, Object> selectPage(int pageNum);
	//갤러리에 사진 추가 
	public void saveGallery(GalleryDto dto);
	public void addToGallery(GalleryDto dto);
	//갤러리 1개의 정보를 삭제하는 메소드 
	public void deleteGallery(int num);

	//갤러리 1개의 정보를 리턴하는 메소드
	public GalleryDto selectOne(int num);
	
}
