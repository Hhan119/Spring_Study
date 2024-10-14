package com.example.boot14;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;

//resources/custom.properties 파일 로딩 
@PropertySource(value="classpath:custom.properties")
@SpringBootApplication
public class Boot14ReactApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(Boot14ReactApiApplication.class, args);
		
		System.out.println("################");
		System.out.println("Boot14_ReartAPI 프로젝트 실행 정상");
		System.out.println("################");
	}

}
