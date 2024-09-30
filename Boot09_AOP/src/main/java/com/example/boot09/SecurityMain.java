package com.example.boot09;

import java.util.Scanner;

import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

// AOP(Aspect Oriented Programming) 관심(관점) 지향 프로그래밍
public class SecurityMain {
	public static void main(String[] args) {
		// 인코딩 할 예정인 비밀번호
		String pwd="1234";
		// 비밀번호를 인코딩할 객체
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
		String encodedPwd = encoder.encode(pwd);
		System.out.println(" 인코딩 된 비밀번호 : " + encodedPwd);
		
		System.out.println("비밀번호 입력");
		// 비밀번호 입력 받기
		String inputPwd = new Scanner(System.in).nextLine();
		// 입력받은 비밀번호와 암호화된 비밀번호의 일치여부 비교
		boolean isVaild = BCrypt.checkpw(inputPwd, encodedPwd);
		if(isVaild) {
			System.out.println("패스워드 일치 합니다.");
		}else {
			System.out.println("패스워드 일치 하지 않습니다.");
		}
	}
}
