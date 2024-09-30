package com.example.boot14.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.boot14.dto.PostDto;
import com.example.boot14.repository.PostDao;
/*
 * 	[Service]
 * 	- 주로 dao 객체를 주입 받아서 사용한다.
 *  - 복잡한 비즈니스 로직을 여기서 처리한다.
 *  - 트랜잭션도 처리할 수 있다.
 *  - 컨트롤러에서 사용하는 유틸리티라고 생각하면 됨.
 */
// 서비스는 @Service 어노테이션을 이용해서 bean으로 만든다. 
@Service
public class PostServiceImpl implements PostService{
	// 서비스는 주고 dao에 의존한다.
	@Autowired
	private PostDao dao;
	
	
	@Override
	public List<PostDto> selectAll() {
		return dao.getList();
	}

	@Override
	public PostDto addContent(PostDto dto) {
		// 글 번호를 미리 얻어낸다.
		int id =dao.getSequence();
		// dto에 글번호를 담은다음 
		dto.setId(id);
		// DB에 저장한다.
		dao.insert(dto);
		// 저장된 글정보를 리턴한다.
		return dto;
	}

	@Override
	public PostDto removeContent(int id) {
		// 삭제할 글정보를 미리 얻어내고 
		PostDto dto = dao.getData(id);
		// DB에서 삭제한다.
		dao.delete(id);
		return dto;
	}

	@Override
	public void updateContent(PostDto dto) {
		// 글 수정
		dao.update(dto);
	}

	@Override
	public PostDto getContent(int id) {
		// id에 해당하는 글 정보 리턴하기 
		return dao.getData(id);
	}

}