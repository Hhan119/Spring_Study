package com.example.boot13.service;

import java.util.List;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import com.example.boot13.dto.MemberDto;
import com.example.boot13.entity.Member;
import com.example.boot13.repository.MemberRepository;

import jakarta.transaction.Transactional;

@Service
public class MemberServiceImpl implements MemberService {
	// JpaRepository 을 상속받아서 만든 MemberRepository 주입받기
	@Autowired
	private MemberRepository repo;
	
	@Override
	public void getList(Model model) {
//		// Entity가 여러개 들어있는 List를 이용해서 List<MemberDto>를 만들어서 Model 객체에 담아야 한다.
//		List<Member> enList = repo.findAll();
//		
//		List<MemberDto> list = new ArrayList<>();
//		// 반복문 돌면서 Member 객체를 순서대로 참조
//		for(Member tmp : enList) {
//			list.add(MemberDto.toDto(tmp));
//		}
//		model.addAttribute("list", list);
		
		// 위 코드를 한줄로 표현
		//List<MemberDto> list = repo.findAll().stream().map(item -> MemberDto.toDto(item)).toList(); //  함수를 만들어서 전달
		List<MemberDto> list = repo.findAllByOrderByNumDesc().stream().map(MemberDto::toDto).toList(); // MemberDto 클래스에 이미 만들어진 함수를 참조해서 전달
		model.addAttribute("list", list);
	}

	@Override
	public void insert(MemberDto dto) {
		 // 1. DTO를 엔티티로 변환
//	    Member member = Member.toEntity(dto);		
//	    repo.save(member);
		
		// 위의 2줄을 한줄로 표현하면 아래와 같다 
		repo.save(Member.toEntity(dto));
	}

	@Override
	public void delete(Long num) {
		// 메소드를 이용해서 삭제
		repo.deleteById(num);
		
	}

	@Override
	public void getData(Long num, Model model) {
		// 회원 번호를 이용해서 Member entity 객체를 얻어낸다.
		Member member = repo.findById(num).get();
		// Entity를 dto로 변환 
		MemberDto dto = MemberDto.toDto(member);
		// model 객체에 담는다.
		model.addAttribute("dto", dto);
	}

	@Override
	public void update(MemberDto dto) {
		// save() 메소드는 insert 와 update 겸용이다.
		repo.save(Member.toEntity(dto));
	}

	/*
	 * Member member1 = repo.findById(변경할 값의 필드).get();
	 * Member member2 = repo.findById(변경할 값의 필드).get();
	 * 
	 * meber1과 member2는 동일한 객체이다. m1 == m2 는 true
	 * 
	 * 만약에 @Transactional이라는 어노테이션이 서비스 메소드에 붙어 있으면,
	 * 해당 메소드 안에서 Entity를 수정하면 수정된 내용을 DB에 실제 반영된다.
	 * (참고로 알아두고 수정할 때는 Repository의 메소드를 이요해서 수정하는 것이 좋다)
	 * 
	 * member1.setName(수정할 이름)
	 * member1.setAddr(수정할 주소)
	 * 
	 * 를 호출하면 실제로 DB에도 반영된다. 
	 * 
	 * 서비스의 특정 메소드를 하나의 Transactional 상에서 실행하려면 @Transactional 이라는 어노테이션을 붙이면 된다.
	 * 
	 */
	@Transactional
	@Override
	public void update2(MemberDto dto) {
		// 수정할 회원의 번호를 이용해서 회원 정보 entity 객체 얻어내기
		Member member1 = repo.findById(dto.getNum()).get();
		Member member2 = repo.findById(dto.getNum()).get();
		boolean isEqual = member1 == member2;
		System.out.println("m1 과 m2 같은지 여부 : "+isEqual);
		// setter 메소드를 이용해서 이름과 주소를 수정하기 
		member1.setName(dto.getName());
		member1.setAddr(dto.getAddr());
	}

}
