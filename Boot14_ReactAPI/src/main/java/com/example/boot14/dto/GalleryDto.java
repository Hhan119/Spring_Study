package com.example.boot14.dto;

import org.apache.ibatis.type.Alias;
import org.springframework.web.multipart.MultipartFile;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Alias("galleryDto") // mybatis에서 사용하는 type alias
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class GalleryDto {
	private int num;
	private String writer;
	private String caption;
	private String saveFileName; // 파일시스템에 저장된 이미지 파일명을 담을 필드
	private String regdate;
	private int startRowNum;
	private int endRowNum;
	// 여러개 파일을 선택해서 받아오려면 MultipartFile[] image 배열로 받아야 한다. 
	private MultipartFile[] images; // 이미지 파일 업로드 처리를 위한 필드
	private int prevNum;
	private int nextNum;
	/*
	 * <input type="file" name="images" multiple>
	 * 이기 때문에 Multipart 배열 type으로 필드를 선언하고 필드명도 images로 변경한다.
	 */ 
}
