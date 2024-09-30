package com.example.boot01;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

import com.example.boot01.service.HomeService;
/*
 * 	 이클래스가 존재하는 페키지(com.example.boot01) 또는 하위 페키지를 모두 scan 해서 
 * 	 spring 이 관리할 객체를 생성하도록 한다 (@SpringBootApplication 어노테이션의 기능)
 */

@SpringBootApplication
public class Boot01BeanApplication {

	public static void main(String[] args) {
		ApplicationContext ctx = SpringApplication.run(Boot01BeanApplication.class, args);
		
		// spring bean container에서 HomeService type을 찾아서 리턴 받는다. 
		HomeService service = ctx.getBean(HomeService.class);
		// 늑대가 집을 청소하면
		service.clean("늑대");
		// 강아지가 빨래를 하려면
		service.wash("강아지");
		// 호랑이가 구멍을 뚤으려면
		service.hole("호랑이");
		
	}
	
}
