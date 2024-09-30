package com.example.boot13.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.boot13.dto.DeptDto;
import com.example.boot13.dto.EmpDeptDto;
import com.example.boot13.entity.Dept;
import com.example.boot13.entity.Emp;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;

@Controller
public class JPQLTestController {

	@Autowired
	private EntityManagerFactory factory;
	
	@GetMapping("/jpql/test")
	public String test() {
		return "jpql/test";
	}
	
	@ResponseBody
	@PostMapping("/jpql/oracle")
	public List<Object[]> oracle(String query){
		// 미리 작성된 query 문을 이용해서 Emp entity 로부터 결과를 얻어와서
		 List<Object[]>  list = null;
		
		EntityManager em =factory.createEntityManager();
		EntityTransaction tx =em.getTransaction();
		tx.begin();
		try {
			// oracle native query 문을 실행 할 수 있도록 .createNativeQuery() 메소드를 활용한다.
			Query q = em.createNativeQuery(query);
			list=q.getResultList();
			tx.commit();
		}catch(Exception e) {
			e.printStackTrace();
			tx.rollback();
		} finally {
			em.close();
		}
		// json으로 응답하기
		return list;
	}
	
	@ResponseBody
	@PostMapping("/jpql/string")
	public List<String> string(String query){
		// 미리 작성된 query 문을 이용해서 Emp entity 로부터 결과를 얻어와서
		 List<String>  list = null;
		
		EntityManager em =factory.createEntityManager();
		EntityTransaction tx =em.getTransaction();
		tx.begin();
		try {
			Query q = em.createQuery(query);
			list=q.getResultList();
			tx.commit();
		}catch(Exception e) {
			e.printStackTrace();
			tx.rollback();
		} finally {
			em.close();
		}
		// json으로 응답하기
		return list;
	}
	
	@ResponseBody
	@PostMapping("/jpql/dto")
	public List<DeptDto> dto(String query){
		// 미리 작성된 query 문을 이용해서 Emp entity 로부터 결과를 얻어와서
		 List<DeptDto>  list = null;
		
		EntityManager em =factory.createEntityManager();
		EntityTransaction tx =em.getTransaction();
		tx.begin();
		try {
			TypedQuery<DeptDto> tQuery = em.createQuery(query, DeptDto.class);
			// generic type이 DZeptDto인 TypedQuery 객체의 getResultList() 를 호출하면
			// List<DeptDto> 객체가 리턴된다.
			list=tQuery.getResultList();
			tx.commit();
		}catch(Exception e) {
			e.printStackTrace();
			tx.rollback();
		} finally {
			em.close();
		}
		// json으로 응답하기
		return list;
	}
	
	@ResponseBody
	@PostMapping("/jpql/array")
	public List<Object[]> array(String query){
		/*
		 *  select 된 row 하나의 정보를 Object[]에 담고
		 *  row 가 여러개이니까 Object[] 객체가 여러개가 필요하고
		 *  해당 객체를 모두 List 담으면 List<Object[]> type이 된다. 
		 */
		// 미리 작성된 query 문을 이용해서 Emp entity 로부터 결과를 얻어와서
		 List<Object[]>  list = null;
		
		EntityManager em =factory.createEntityManager();
		EntityTransaction tx =em.getTransaction();
		tx.begin();
		try {
			// Entity type 을 전달하지 않으면 Query type 이 리턴된다.
			Query q=em.createQuery(query);
			// Query 객체의 getResultList() 메소드가 리턴해주는 type을 사요하면 된다.
			list = q.getResultList();
			tx.commit();
		}catch(Exception e) {
			e.printStackTrace();
			tx.rollback();
		} finally {
			em.close();
		}
		// json으로 응답하기
		return list;
	}
	
	@ResponseBody
	@PostMapping("/jpql/dept")
	public List<DeptDto> dept(String query){
		// 미리 작성된 query 문을 이용해서 Emp entity 로부터 결과를 얻어와서
		List<DeptDto> list = null;
		
		EntityManager em =factory.createEntityManager();
		EntityTransaction tx =em.getTransaction();
		tx.begin();
		try {
			// .createQuery() 메소드의 2번째 인자로 전달한 Entity 클래스가 리턴되는 TypeQuery 객체의 Generic type이 된다.
			TypedQuery<Dept> tQuery = em.createQuery(query, Dept.class);
			// TypedQuery 객체를 stream으로 만들어서 List<EmpDeptDto> 객체를 얻어낸다.
			list=tQuery.getResultStream().map(DeptDto::toDto).toList();
			tx.commit();
		}catch(Exception e) {
			e.printStackTrace();
			tx.rollback();
		} finally {
			em.close();
		}
		// json으로 응답하기
		return list;
	}
	
	@ResponseBody
	@PostMapping("/jpql/emp")
	public List<EmpDeptDto> emp(String query){
		// 미리 작성된 query 문을 이용해서 Emp entity 로부터 결과를 얻어와서
		List<EmpDeptDto> list = null;
		
		EntityManager em =factory.createEntityManager();
		EntityTransaction tx =em.getTransaction();
		tx.begin();
		try {
			// .createQuery() 메소드의 2번째 인자로 전달한 Entity 클래스가 리턴되는 TypeQuery 객체의 Generic type이 된다.
			TypedQuery<Emp> tQuery = em.createQuery(query, Emp.class);
			// TypedQuery 객체를 stream으로 만들어서 List<EmpDeptDto> 객체를 얻어낸다.
			list=tQuery.getResultStream().map(EmpDeptDto::toDto).toList();
			tx.commit();
		}catch(Exception e) {
			e.printStackTrace();
			tx.rollback();
		} finally {
			em.close();
		}
		// json으로 응답하기
		return list;
	}
}
