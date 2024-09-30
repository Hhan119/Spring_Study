package com.example.boot11.exception;

// 인셉션 발생시키려면 extends해서 RuntimeException 상속받으면 됨
public class NotOwnerException extends RuntimeException{
	//생성자
	public NotOwnerException(String message) {
		super(message);
	}
}
