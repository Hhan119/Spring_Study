package com.example.boot14.repository;

import java.util.List;

import com.example.boot14.dto.PostDto;

public interface PostDao {
	// 글 전체 목록을 가지고 오는 메소드
	public List<PostDto> getList();
	// id 값으로 글 하나의 정보를 가지고 오는 메소드 
	public PostDto getData(int id);
	// 글 추가하는 메소드
	public void insert(PostDto dto);
	// 글 업데하는 메소드 
	public void update(PostDto dto);
	// 글 id를 통해서 삭제 하는 메소드
	public void delete(int id);
	public int getSequence();
	

}
