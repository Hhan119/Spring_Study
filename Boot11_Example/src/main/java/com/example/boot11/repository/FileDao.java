package com.example.boot11.repository;

import java.util.List;

import com.example.boot11.dto.FileDto;

public interface FileDao {
	// 파일 전체 목록 불러오기
	public List<FileDto> getList(FileDto dto); // pageNum에 해당하는 파일 목록 리턴하기
	// 파일 하나의 정보 리턴
	public FileDto getData(int num);
	// 파일정보 저장
	public void insert(FileDto dto);
	// 파일 정보 수정
	public void update(FileDto dto);
	// 하나의 파일 정보 삭제 
	public void delete(int num);
	// 페이징 처리
	public int getCount(FileDto dto);
}
