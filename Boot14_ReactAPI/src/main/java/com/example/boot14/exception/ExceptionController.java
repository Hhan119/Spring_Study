package com.example.boot14.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

// 예외를 처리하는 컨트롤러는 @@ControllerAdvice 어노테이션을 붙여서 만든다. 
@ControllerAdvice
public class ExceptionController {
	@ExceptionHandler(PasswordException.class)
	public ResponseEntity<Object> passwordException(PasswordException pe){
		// 예외 메세지 
		String message = pe.getMessage();
		// 에러 응답객체 
		SimpleErrorResponse response = SimpleErrorResponse.builder().code(401).message(message).build();
		// 		return new ResponseEntity<>(response, HttpStatus.OK); // 200 번 정상적인 응답

		return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED); // 401 에러 응답 
	}

}
