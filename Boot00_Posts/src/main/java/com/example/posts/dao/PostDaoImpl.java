package com.example.posts.dao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.example.posts.dto.PostDto;

public class PostDaoImpl implements PostDao{
	@Autowired
	private PostDao dao;
	
	@Override
	public List<PostDto> selectList() {
		List<PostDto> list = dao.selectList();
		return list;
	}

	@Override
	public void insert(PostDto dto) {
		// TODO Auto-generated method stub
		dao.insert(dto);
	}

	@Override
	public void delete(Long id) {
		dao.delete(id);
	}

	@Override
	public PostDto getData(Long id) {
		PostDto dto = dao.getData(id);
		return dto;
	}

	@Override
	public void update(PostDto dto) {
		dao.update(dto);
	}

}
