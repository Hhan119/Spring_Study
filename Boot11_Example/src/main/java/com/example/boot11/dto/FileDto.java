package com.example.boot11.dto;

import org.apache.ibatis.type.Alias;
import org.springframework.web.multipart.MultipartFile;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Alias("fileDto") 
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class FileDto {
private int num;
private String writer;
private String title;
//원본 파일명
private String orgFileName;
//파일 시스템에 저장된 파일명
private String saveFileName;
//파일의 크기 
private long fileSize;
private String regdate;
//파일 업로드 처리를 하기 위한 필드
private MultipartFile myFile;
//페이징 처리를 위한 필드
private int pageNum=1; // 페이지 번호 디폴트 값 1
private int startRowNum;
private int endRowNum;
// 검색 키둬드 관련 필드 
// 검색조건이 없는경우 null 이 출력되는걸 방지 하기위해 방문자열을 기본값으로 설정
private String condition=""; 
private String keyword="";

		
}
