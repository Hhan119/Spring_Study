package com.example.boot14.repository;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.example.boot14.dto.PostDto;

// dao를 bean으로 만들기 위한 어노테이션 
@Repository
public class PostDaoImpl implements PostDao{

	// mybatis 기반으로 select, insert, update, delete 작업을 하기 위한 핵심 의존객체
	@Autowired
	private SqlSession session;
	
	@Override
	public List<PostDto> getList() {
		/*
		 * mapper`s namespace : post
		 * sql`s id : getList 
		 * resultType : PostDto
		 * parameterType : x  
		 */
		// return의 selectList 값은 List<PostDto> type임(제네릭 type은 postmapper.xml에 resultType의 값임)
		return session.selectList("post.getList");
	}

	@Override
	public PostDto getData(int id) {
		/*
		 * mapper`s namespace : post
		 * sql`s id : getData 
		 * resultType : PostDto
		 * parameterType : int  
		 */
		return session.selectOne("post.getData", id);
	}

	@Override
	public void insert(PostDto dto) {	
		/*
		 * mapper`s namespace : post
		 * sql`s id : insert 
		 * resultType : select 가 아니기 때문에 resultType은 없다!
		 * parameterType : PostDto  
		 */
		session.insert("post.insert", dto);
		
	}

	@Override
	public void update(PostDto dto) {
		/*
		 * mapper`s namespace : post
		 * sql`s id : update 
		 * resultType : select 가 아니기 때문에 resultType은 없다!
		 * parameterType : PostDto  
		 */
		session.update("post.update", dto);
	}

	@Override
	public void delete(int id) {
		/*
		 * mapper`s namespace : post
		 * sql`s id : delete 
		 * resultType : select 가 아니기 때문에 resultType은 없다!
		 * parameterType : int  
		 */
		session.delete("post.delete", id);
	}
	
	// 시퀀스 숫자 하나를 가져오는 메소드 
	@Override
	public int getSequence() {
		/*
		 * mapper`s namespace : post
		 * sql`s id : getSequence 
		 * resultType : int 
		 * parameterType : 없다  
		 */
		 return session.selectOne("post.getSequence");
	}

}
