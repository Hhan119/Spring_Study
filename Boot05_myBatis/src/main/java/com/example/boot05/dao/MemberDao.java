package com.example.boot05.dao;

import java.util.List;

import com.example.boot05.dto.MemberDto;

// spring에서는 의존성을 느슨하게 생성하기 위해 인터페이스로 생성하여 정의한다.(구현은 별도로 구성)
public interface MemberDao {
	public List<MemberDto> selectList();
	public void insert(MemberDto dto);
	public void delete(int num);
	public MemberDto getData(int num);
	public void update(MemberDto dto);
	
}
