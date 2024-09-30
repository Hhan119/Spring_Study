package com.example.boot13.dto;

import java.util.List;

import com.example.boot13.entity.Dept;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class DeptDto {

	private int deptno;
	private String dname;
	private String loc;
	private int count; // 근무하는 인원수 
	private List<String> names; // 근무하는 사원의 이름들
	
	// jpql에서 테스트 할 생성자 만들기
	public DeptDto(int deptno, String dname, String loc) {
		this.deptno=deptno;
		this.dname=dname;
		this.loc=loc;
	}
	
	//Entity 를 Dto 로 변환하는 메소드
	public static DeptDto toDto(Dept dept) {
		
		//근무하는 사원의 숫자는 List<Emp> 의 size() 값이다 
		int count = dept.getList().size();
		// List<Emp> 목록을 이용해서 사원이름 목록을 얻어내기 List<String> 
		List<String> names=dept.getList().stream().map(item->item.getEname()).toList();
		
		return DeptDto.builder()
				.deptno(dept.getDeptno())
				.dname(dept.getDname())
				.loc(dept.getLoc())
				.count(count)
				.names(names)
				.build();
	}
}









