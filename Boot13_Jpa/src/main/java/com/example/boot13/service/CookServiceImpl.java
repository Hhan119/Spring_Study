package com.example.boot13.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import com.example.boot13.dto.CookDto;
import com.example.boot13.dto.MemberDto;
import com.example.boot13.entity.Cook;
import com.example.boot13.entity.Member;
import com.example.boot13.repository.CookRepository;

@Service
public class CookServiceImpl implements CookService{
	@Autowired
	private CookRepository repo;
	
	@Override
	public void getList(Model model) {
		List<CookDto> list = repo.findAll().stream().map(CookDto :: toDto).toList();
		model.addAttribute("list", list);
	}

	@Override
	public void getData(Long num, Model model) {
		// 회원 번호를 이용해서 Member entity 객체를 얻어낸다.
		Cook cook = repo.findById(num).get();
		// Entity를 dto로 변환 
		CookDto dto = CookDto.toDto(cook);
		// model 객체에 담는다.
		model.addAttribute("dto", dto);
			
	}

	@Override
	public void save(CookDto dto) {
		Cook cook = Cook.toEntity(dto); 
		repo.save(cook);
	}

	@Override
	public void delete(Long num) {
		repo.deleteById(num);
	}

	@Override
	public void update(CookDto dto) {
		repo.save(Cook.toEntity(dto));
	}

}
