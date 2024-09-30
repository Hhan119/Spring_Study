package com.example.boot11.service;


import org.springframework.core.io.InputStreamResource;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;

import com.example.boot11.dto.FileDto;


public interface FileService {
	// 파일 목록을 Model 객체에 담아주는 메소드
	public void getList(Model model, FileDto dto);
	// 파일 정보를 저장하는 메소드
	public void saveFile(FileDto dto);
	// 다운로드 해줄 파일 하나의 정보 얻어오기
	public ResponseEntity<InputStreamResource> getFileData(int num);
	// 파일 삭제
	public void deleteFile(int num);
}
