package com.example.boot14.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@RestController
public class TestController {
	//파일 저장경로
	@Value("${file.location}")
	private String fileLocation;
	
	//요청 파리미터를 한번에 추출하기 위한 클래스 정의하기(dto 의 역활을 한다)
	@Setter
	@Getter
	class FileDownRequest{
		String saveFileName;
		String orgFileName;
		long fileSize;
	}
	
	@Data
	class UploadRequest{
		String title;
		MultipartFile image;
	}
	
	@GetMapping("/file/download")
	public ResponseEntity<InputStreamResource> download(FileDownRequest dto) throws UnsupportedEncodingException, FileNotFoundException{
		//요청 파라미터가 FileDownRequest 에 담겨서 전달된다.
		//다운로드 시켜줄 원본 파일명
		String encodedName=URLEncoder.encode(dto.getOrgFileName(), "utf-8");
		//파일명에 공백이 있는경우 파일명이 이상해지는걸 방지
		encodedName=encodedName.replaceAll("\\+"," ");
		//응답 헤더정보(스프링 프레임워크에서 제공해주는 클래스) 구성하기 (웹브라우저에 알릴정보)
		HttpHeaders headers=new HttpHeaders();
		//파일을 다운로드 시켜 주겠다는 정보
		headers.add(HttpHeaders.CONTENT_TYPE, "application/octet-stream"); 
		//파일의 이름 정보(웹브라우저가 해당정보를 이용해서 파일을 만들어 준다)
		headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename="+encodedName);
		//파일의 크기 정보도 담아준다.
		headers.setContentLength(dto.getFileSize());
		
		//읽어들일 파일의 경로 구성
		String filePath=fileLocation + File.separator + dto.getSaveFileName();
		//파일에서 읽어들일 스트림 객체
		InputStream is=new FileInputStream(filePath);
		//InputStreamResource 객체의 참조값 얻어내기
		InputStreamResource isr=new InputStreamResource(is);
		
		//ResponseEntity 객체의 참조값 얻어내기 
		ResponseEntity<InputStreamResource> resEn=ResponseEntity.ok()
			.headers(headers)
			.body(isr);
		
		return resEn;
		
	}
	
	
	@PostMapping("/file/upload")
	public Map<String, Object> fileUpload(MultipartFile myFile){
		String orgFileName=myFile.getOriginalFilename();
		String saveFileName=UUID.randomUUID().toString();
		//파일의 크기
		long fileSize=myFile.getSize();
		//파일시스템에 저장할 경로
		String filePath=fileLocation+File.separator+saveFileName;
		//파일객체 생성
		File dest=new File(filePath);
		try {
			//업로드된 파일를 원하는 곳으로 전송(copy) 
			myFile.transferTo(dest);
		}catch(Exception e) {}
		Map<String, Object> map=new HashMap<String, Object>();
		map.put("orgFileName", orgFileName);
		map.put("saveFileName", saveFileName);
		map.put("fileSize", fileSize);
		
		return map;
	}

	//이미지 업로드 테스트
	@PostMapping("/image/upload")
	public Map<String, Object> imageUpload(MultipartFile image){
		//원본 파일명
		String orgFileName=image.getOriginalFilename();
		//저장할 파일명 구성
		String saveFileName=UUID.randomUUID().toString();
		//파일시스템에 저장할 경로
		String filePath=fileLocation+File.separator+saveFileName;
		//파일객체 생성
		File dest=new File(filePath);
		try {
			//업로드된 이미지를 원하는 곳으로 전송(copy) 
			image.transferTo(dest);
		}catch(Exception e) {}
		//업로드된 정보를 json 으로 응답하기 위해 
		Map<String, Object> map=new HashMap<>();
		map.put("orgFileName", orgFileName);
		map.put("saveFileName", saveFileName);
		return map;
	}
	
	
	//이미지 업로드 테스트
		@PostMapping("/image/upload2")
		public Map<String, Object> imageUpload2(UploadRequest request){
			// UploadRequest 객체에 title 과 image 정보가 한번에 담긴다.
			MultipartFile image= request.getImage();
			// 입력한 제목
			String title=request.getTitle();
			//원본 파일명
			String orgFileName=image.getOriginalFilename();
			//저장할 파일명 구성
			String saveFileName=UUID.randomUUID().toString();
			//파일시스템에 저장할 경로
			String filePath=fileLocation+File.separator+saveFileName;
			//파일객체 생성
			File dest=new File(filePath);
			try {
				//업로드된 이미지를 원하는 곳으로 전송(copy) 
				image.transferTo(dest);
			}catch(Exception e) {}
			//업로드된 정보를 json 으로 응답하기 위해 
			Map<String, Object> map=new HashMap<>();
			map.put("orgFileName", orgFileName);
			map.put("saveFileName", saveFileName);
			map.put("title", title);
			return map;
		}
}






















