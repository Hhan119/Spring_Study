package com.example.boot14.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.boot14.dto.MemberDto;
import com.example.boot14.repository.MemberDao;

@Service
public class MemberServiceImpl implements MemberService {
	@Autowired
	private MemberDao dao;
	
	@Override
	public MemberDto addMember(MemberDto dto) {
		// 글 번호를 미리 얻어낸다.
		int num = dao.getSequence();
		// dto에 글번호를 담은다음 
		dto.setNum(num);
		// DB에 저장한다.
		dao.insert(dto);
		// 저장된 글정보를 리턴한다.
		return dto;
	}

	@Override
	public void updateMember(MemberDto dto) {
		dao.update(dto);
	}

	@Override
	public void deleteMember(int num) {
		// 삭제할 글정보를 미리 얻어내고 
		MemberDto dto = dao.getData(num);
		dao.delete(num);
	}

	@Override
	public MemberDto selectOneMember(int num) {
		return dao.getData(num);
	}

	@Override
	public List<MemberDto> selectListMember() {
		List<MemberDto> list = dao.getList();
		return list;
	}



}
