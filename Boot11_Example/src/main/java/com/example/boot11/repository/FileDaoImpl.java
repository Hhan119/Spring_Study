package com.example.boot11.repository;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.example.boot11.dto.FileDto;


@Repository
public class FileDaoImpl implements FileDao{
	@Autowired
	private SqlSession session;
	
	
	@Override
	public List<FileDto> getList(FileDto dto) {
		// FileDto 에는 보여줄 page에 해당하는 startRowNum과 endRowNum이 들어있다.
		List<FileDto> list = session.selectList("file.getList", dto);
		return list;
	}

	@Override
	public FileDto getData(int num) {
		return session.selectOne("file.getData", num);
	}

	@Override
	public void insert(FileDto dto) {
		session.insert("file.insert", dto);
	}

	@Override
	public void update(FileDto dto) {
		session.update("file.update", dto);
	}

	@Override
	public void delete(int num) {
		session.delete("file.delete", num);
	}

	@Override
	public int getCount(FileDto dto) {
		// dto에 condition 과 keyword는 null일수도 있고 아닐수도 있다. 
		return session.selectOne("file.getCount", dto);
	}

}