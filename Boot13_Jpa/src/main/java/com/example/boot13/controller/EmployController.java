package com.example.boot13.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.boot13.dto.DeptDto;
import com.example.boot13.dto.EmpDeptDto;
import com.example.boot13.entity.Dept;
import com.example.boot13.entity.Emp;
import com.example.boot13.repository.DeptRepository;
import com.example.boot13.repository.EmpRepository;

@Controller
public class EmployController {
	//테스트의 편의를 위해 서비스 만들지 않고 바로 Repository 객체 활용하기
	@Autowired
	private EmpRepository empRepo;
	@Autowired
	private DeptRepository deptRepo;
	
	@GetMapping("/emp/dept_detail")
	public String deptDetail(int deptno, Model m) {
		//부서번호를 이용해서 Dept 객체 얻어내기
		Dept dept=deptRepo.findById(deptno).get();
		
		//Entity 를 dto 로 변형해서 
		DeptDto dto = DeptDto.toDto(dept);
		
		//Model 객체에 담는다.
		m.addAttribute("dto", dto);
		
		return "emp/dept_detail";
	}
	@GetMapping("/emp/list4")
	public String list4(Model model, String keyword) {
		// 검색 keyword 가 이름(ename) 검색 이라고 가정하자
		keyword="A";
//		List<EmpDeptDto> list = empRepo.findByEnameContaining(keyword)
//									.stream().map(EmpDeptDto::toDto).toList();
		
//		List<EmpDeptDto> list = empRepo.findByEnameContainingOrJobContaining(keyword, keyword)
//									.stream().map(EmpDeptDto::toDto).toList();
		
		// 부서번호에 대해서 오름차순, sal 에 대해서 내림차순
		Sort sort=Sort.by(Order.asc("dept.deptno"), Order.desc("sal"));
		// 1 페이지 내용, 한페이지에 5개, 정렬 
		Pageable pageable = PageRequest.of(1-1, 5, sort);
		
		List<EmpDeptDto> list = empRepo.findByEnameContaining(keyword, pageable)
									.stream().map(EmpDeptDto::toDto).toList();
		model.addAttribute("list", list);
		return "emp/list4";
	}
	
	@GetMapping("/emp/list3")
	public String list3(@RequestParam(defaultValue = "1") int pageNum, Model model) {
		// 한페이지에 나타낼 row의 갯수
		final int PAGE_ROW_COUNT=5;
		// 하단 페이지 표시 갯수
		final int PAGE_DISPLAY_COUNT=5;
		
		// empno에 대해서 오름차순 정렬하겠다는 정보를 담고 있는 Sort 객체 생성
		Sort sort=Sort.by(Sort.Direction.ASC, "empno");
		// 원하는 페이지 정보를 담고 있는 Pageable 객체를 얻어내서
		Pageable pageable = PageRequest.of(pageNum-1, PAGE_ROW_COUNT, sort);
		// JpaRepository 객체에 전달해서 원하는 페이지 정보를 얻어낸다.
		Page<Emp> page=empRepo.findAll(pageable);
		// Page<Emp> 객체를 List<EmpDeptDto> 객체로 변환하기
		List<EmpDeptDto> list= page.stream().map(EmpDeptDto::toDto).toList();
		
		//하단 시작 페이지 번호 
		int startPageNum = 1 + ((pageNum-1)/PAGE_DISPLAY_COUNT)*PAGE_DISPLAY_COUNT;
		//하단 끝 페이지 번호
		int endPageNum=startPageNum+PAGE_DISPLAY_COUNT-1;
		//전체 페이지의 갯수 구하기
		int totalPageCount=page.getTotalPages();
		//끝 페이지 번호가 이미 전체 페이지 갯수보다 크게 계산되었다면 잘못된 값이다.
		if(endPageNum > totalPageCount){
			endPageNum=totalPageCount; //보정해 준다. 
		}
				
		//pageNum 에 해당하는 사원 목록을 Model 에 담는다 
		model.addAttribute("list", list);
		model.addAttribute("startPageNum", startPageNum);
		model.addAttribute("endPageNum", endPageNum);
		model.addAttribute("totalPageCount", totalPageCount);
		model.addAttribute("pageNum", pageNum);
		
		return "emp/list3";
	}
	@GetMapping("/emp/list2")
	public String list2(Model m, int pageNum){
		// 한페이지에 몇개씩 표시할 것인지
		final int PAGE_ROW_COUNT=5;
		// 도메인 패키지 임폴트 
		Sort sort=Sort.by(Sort.Direction.ASC, "empno"); // 사원번호에 대해서 오름차순 정렬
		/*
		 * .of(페이지 인덱스, 한페이지에 나타탈 갯수, 정렬 객체)
		 * PageRequest 객체가 리턴되는데 PageRequest는 Pageable 인터페이스를 구현한 객체이다.
		 */
		Pageable pageable = PageRequest.of(pageNum-1, PAGE_ROW_COUNT, sort);
		// pageable 객체를 이용해서 해당 페이지에 맞는 정보를 얻어낸다.
		Page<Emp> page=empRepo.findAll(pageable);
		
		// Page<Emp> 를 stream으로 만들어서 map() 함수를 이용해서 List<EmpListDto>로 변환하기
		List<EmpDeptDto> list= page.stream().map(EmpDeptDto::toDto).toList();
		m.addAttribute("list", list);
		return "emp/list2";
		
	}
	@GetMapping("/emp/list")
	public String list(Model m) {
		//응답에 필요한 데이터를 얻어와서 
		//List<EmpDto> list=empRepo.findAll().stream().map(EmpDto::toDto).toList();
		//List<EmpDeptDto> list=empRepo.findAll().stream().map(EmpDeptDto::toDto).toList();
		
		// JpaRepository에 존재하는 메소드를 이용해서 목록 얻어오기
		//List<Emp> empList =empRepo.findAll();
		
		// EmpRepository에 직접 추가한 메소드를 이용해서 목록 얻어오기
		//List<Emp> empList =empRepo.findAllByOrderByEmpnoAsc(); // 오름차순
		//List<Emp> empList =empRepo.findAllByOrderByEmpnoDesc(); // 내림차순
		
		// sal 이 2000 이상인 사원의 목록
		List<Emp> empList=empRepo.getList(2000);
		
		// List<Emp>을 stream 으로 만들어서 map 함수를 이용해서 List<EmpDeptDto>로 변환시켜주는 작업  
		List<EmpDeptDto> list = empList.stream().map(EmpDeptDto::toDto).toList();
		//Model 객체에 담고
		m.addAttribute("list", list);
		// 템플릿 페이지에서 사원목록 응답
		return "emp/list";
	}
}











