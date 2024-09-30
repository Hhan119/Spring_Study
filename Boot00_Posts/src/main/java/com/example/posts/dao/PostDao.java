package com.example.posts.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.example.posts.dto.PostDto;


@Mapper
public interface PostDao {
	public List<PostDto> selectList();
	public void insert(PostDto dto);
	public void delete(Long id);
	public PostDto getData(Long id);
	public void update(PostDto dto);
}
