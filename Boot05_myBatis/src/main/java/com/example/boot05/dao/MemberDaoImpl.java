package com.example.boot05.dao;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.example.boot05.dto.MemberDto;

@Repository
public class MemberDaoImpl implements MemberDao {

	// Mabatis로 sql문 작성(mybatis에서 사용하면 코드량을 적어진다.)
	@Autowired
	private SqlSession session;
	
	@Override
	public List<MemberDto> selectList() {
		/*
		 * 	SqlSession 객체를 이용해서 회원목록을 얻어온다.
		 */
		List<MemberDto> list =session.selectList("member.selectList");
		return list;

	}

	@Override
	public void insert(MemberDto dto) {
		/*
		 * 	SqlSession 객체를 이용해서 MemberDto 객체에 담긴 정보를 DB에 저장하기
		 */
		session.insert("member.insert", dto);
	}

	@Override
	public void delete(int num) {
		session.delete("member.delete", num);
		
	}

	@Override
	public MemberDto getData(int num) {
		MemberDto dto = session.selectOne("member.getData", num);
		return dto;
	
	}

	@Override
	public void update(MemberDto dto) {
		/*
		 * mapper의 namespace => member
		 * sql의 id => update
		 * parameter type => MemberDto
		 */
		session.update("member.update", dto);
	}

	
}
