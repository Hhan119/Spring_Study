package com.example.boot09.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

/*
 *	void => return type이 void
 *	send* => send로 시작하는 메소드명
 *	(..) => 매개변수는 없어도 되고 여러개여도 되고, type은 상관이 없다. 	
 */
@Aspect
@Component
public class MessengerAspect {
	// 매개변수 type을 java.lang.String, java.lang.Integer 이런식으로 쓸수가 있다.
	// 하지만, (..)을 하면 type을 가리지 않고 사용한다. 
	@Around("execution(void send*(..))")
	public void checkGreeting(ProceedingJoinPoint joinPoint) throws Throwable{
		// 메소드에 전달된 인자들 목록을 얻어내기
		Object[] args = joinPoint.getArgs();
		// 반복문 돌면서 매개변수에 담긴 값들을 하나하나 조사 해볼수가 있다.
		for(Object tmp:args) {
			// 찾는 type을 확인한다.
			if(tmp instanceof String) {
				// 원래 type으로 casting 해서 작업한다.
				String msg=(String)tmp;
				System.out.println("aspect에서 읽어낸 내용 : "+msg);
				if(msg.contains("똥개")) {
					System.out.println("똥깨는 금지된 단어입니다.");
					return ; // 메소드를 여기서 끝내기 
				}
				
			}
		}
		// 이 메소드를 호출하는 시점에 실제로 aspect가 적용된 메소드가 수행된다.(호출하지 않으면 수행 안됨)
		joinPoint.proceed();
		
		// aspect가 적용된 메소드가 리턴된 이후에 할 작업은 proceed() 호출 이후에 한다.
		System.out.println("aspect가 적용된 메소드가 리턴 했습니다.");
	}
	
	/*
	 * 	return type은 String 이고(java.lang 패키지에 있는 데이터 type은 패키지명 생략 가능)
	 * 	get으로 시작되는 메소드이고,
	 * 	메소드에 전달되는 인자는 없다.
	 * 	java.lang 패키지에 있는 type은 패키지명 생략 가능
	 * 	com.example.boot09.util 패키지에 존재하는 모든 클래스의 메소드 중에서 get으로 시작하는 메소드
	 */
	@Around("execution(String com.example.boot09.util.*.get*())")
	public Object checkReturn(ProceedingJoinPoint joinPoint) throws Throwable {
		// aspect가 적용된 메소드를 수행하고 리턴되는 데이터 받아오기
		Object obj = joinPoint.proceed();
		// 원래 type으로 casting 해서 조사해 볼수가 있다.
		String a = (String)obj;
		
		// 조사 후 아예 다른 값을 리턴 해줄수도 있다.
		// return a+"요!";
		return "공부해!";
		
	}
}
