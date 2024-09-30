package com.example.boot14;


import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.example.boot14.dto.MemberDto;
import com.example.boot14.repository.MemberDao;

import jakarta.transaction.Transactional;

// spring boot application을 테스트 하기 위한 어노테시연
@SpringBootTest
@Transactional
class Boot14ReactApiApplicationTests {

	@Autowired MemberDao dao;
	
	// @Test 어노테이션을 이용해서 테스트 case 메소드를 작성한다.
	@Test
	public void Hello() {
		int sum = 1+2;
		assertEquals(sum, 2);
	}
	@Test
	void testIsNull() {
		String str = null;
		// 반드시 null이여야 하는 단인
		// null이면 pass, nuyll이 아니면 fail
		assertNull(str);
	}
	
	@Test
	void testNotNull() {
		String str ="chelsea";
		// 반드시 null이 아니여야 하는 단인
		// null이 아니면 pass, null이면 fail
		assertNotNull(str);
	}
	
	@Test
	void testIsTrue() {
		boolean isRun = false;
		// 반드시 true여야 한다는 단인
		// true면 pass, false면 fail
		assertTrue(isRun);
		assertTrue(isRun, "달릴지 여부는 true여야 한다.");

	}

	@Test
	void testMemberDaoNotNull() {
		// 실제로 dao가 null이 아닌지 여부를 알 수 있다.
		assertNotNull(dao);
	}
	
	/*
	 * name : test_name
	 * addr : test_addr
	 * 인 회원정보를 저장하고 작업의 성공여부 테스트하기 
	 */
	@Test
	void testMemberDaoInsert() {
		// 회원 번호를 얻어낸다.
		int num=dao.getSequence();
		// 저장할 회원 정보를 MemberDto에 담는다.
		MemberDto dto = MemberDto.builder()
				.num(num).name("test_name").addr("test_addr").build();
		// DB에 저장한다.
		dao.insert(dto);
		// 저장된 결과를 다시 select 한다.
		MemberDto savedDto=dao.getData(num);
		assertEquals(savedDto.getName(), "test_name");
		assertEquals(savedDto.getAddr(), "test_addr");
		
	}
}
