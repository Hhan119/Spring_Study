package com.example.boot14.repository;

import java.util.List;

import com.example.boot14.dto.CafeDto;


public interface CafeDao {
	// pageNum에 해당하는 글목록을 리턴하는 메소드
	public List<CafeDto> getList(CafeDto dto);
	// 글 번호에 해당하는 글정보 리턴 메소드
	public CafeDto getData(int num);
	// 검색 조건에 해당하는 그의 갯수를 리턴하는 메소드
	public int getCount(CafeDto dto);
	// 글 추가 메소드
	public void insert(CafeDto dto);
	// 검색 조건과 글번호를 CafeDto에 전달하면,
	public CafeDto getDetail(CafeDto dto);
	// 글 삭제 메소드
	public void delete(int num);
	// 글 수정 메소드 
	public void update(CafeDto dto);
}
