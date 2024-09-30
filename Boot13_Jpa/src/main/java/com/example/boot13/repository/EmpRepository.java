package com.example.boot13.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.boot13.entity.Emp;

public interface EmpRepository extends JpaRepository<Emp, Integer>{

	/*
	 * FindAll => 모든 목록
	 * ByOrder => 정렬 
	 * ByEmpnoAsc => 사원번호에 대해서 오름차순
	 * 
	 * Java Persistence Query Language (JPQL)
	 * 	- JPQL은 SQL과 유사하지만 엔티티와 속성에 기반해서 작성되며, 데이터베이스 종속적이지 않음
	 * 	- JPQL 만의 문법이 존재한다.
	 *  - 테이블명 대신에 => Entity 명
	 *  - Entity 의 별칭은 필수
	 *  - select 된 row의 정보를 Entity 혹은 Dto에 담을 수 있다. 
	 */
	public List<Emp> findAllByOrderByEmpnoAsc();
	public List<Emp> findAllByOrderByEmpnoDesc();
	
	// 실행할 query 실행문 및 메소드명도 원하는대로 지정을 할 수 있다.
	@Query(value = "select e from Emp e order by e.empno asc")
	public List<Emp> getListAll();
	
	// select 인자를 전달할 수 있다. :변수명
	@Query(value = "select e from Emp e where sal > :sal order by e.sal asc")
	public List<Emp> getList(int sal);
	
	// select 인자를 여러개 전달할 수 있다. :변수명, :변수명2
	@Query(value = "select e from Emp e where sal > :sal and sal < :sal2 order by e.sal asc")
	public List<Emp> getListBetWeen(int sal, int sal2);
	
	// select 인자를 순서대로 전달할 수 있다. ?1, ?2 여기서 1, 2 는 순서임
	@Query(value = "select e from Emp e where sal > ?1 and sal < ?2 order by e.sal asc")
	public List<Emp> getListBetween2(int sal, int sal2);
	
	/*
	 * 	검색 기워드를 반영하는 메소드
	 * ename에 keyword가 포함된 목록 select
	 * 
	 * EnameContaining => 사원이름에 keyword가 포함된 
	 */
	public List<Emp> findByEnameContaining(String keyword);

	// EnameContainingOrJobContaining => 사원이름 또는 직책에 keyword가 포함됨
	public List<Emp> findByEnameContainingOrJobContaining(String keyword, String keyword2);
	
	/*
	 * 페이징 처리와 검색 키워드를 같이 처리 할 수도 있다. 
	 * 대신 return type은 List가 아닌 Page 객체이다.
	 */
	
	public Page<Emp> findByEnameContaining(String keyword, Pageable pageable);
}