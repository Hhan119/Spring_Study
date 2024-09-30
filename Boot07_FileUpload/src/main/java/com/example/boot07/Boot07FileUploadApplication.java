package com.example.boot07;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;

import com.example.boot07.dto.FileDto;

/*
 * 	@PropertySource(value="커스텀 properties 파일의 위치")
 * 	classpath: 는 resources 폴더를 가리킨다.
 */
@PropertySource(value="classpath:custom.properties")
@SpringBootApplication
public class Boot07FileUploadApplication {

	public static void main(String[] args) {
		SpringApplication.run(Boot07FileUploadApplication.class, args);
		// lombok의 기능 테스트 
		FileDto dto = new FileDto();
		dto.setNum(1);
		dto.setWriter("첼시");
		FileDto dto2 = FileDto.builder().num(1).writer("첼시").title("1위팀").build();
		
	}
}
