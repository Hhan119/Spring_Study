package com.example.boot13.entity;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Dept {
	@Id
	private Integer deptno;
	private String dname;
	private String loc;
	
	//해당 부서에 속해 있는 사원의 목록 
	@OneToMany(mappedBy = "dept", fetch = FetchType.EAGER)
	private List<Emp> list=new ArrayList<>();
}











