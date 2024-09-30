package com.example.boot11.exception;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

// 예외 컨트롤러는 @ControllerAdvice 어노테이션을 붙여서 bean으로 만든다.
@ControllerAdvice
public class ExceptionController {

	/*
	 * 	스프링 프레임워크가 동작하는 중에 PasswordException type의 예외가 발생하면 이 메소드가 자동 호출된다.
	 * 	매개변수에는 해당 예외객체의 참조값이 전달된다.
	 * 	일반 컨트롤러처럼 필요한 객체를 매개변수에 선언하면 스프링이 알아서 전달해준다. 
	 * 	템플릿 페이지로 forward 이동해서 응답 할 수도 이쏙, 리디렉션 응답 할 수도 있다.
	 */
//	@ExceptionHandler(PasswordException.class)
//	public String password(PasswordException pe, Model model) {
//	/*
//	 * 	"exception" 이라는 키값으로 예외 객체를 담으면
//	 * 	템플릿 페이지에서 예외 객체는 ${exception}으로 참조 할 수도 있고,
//	 * 	예외 메세지는 ${exception.message}로 읽어낼 수가 있다.
//	 * 	.message는 getter 메소드 즉, .getMessage()를 호출 하는 것이다. 
//	 */
//		model.addAttribute("exception", pe);
//		// /templates/error/password.html 템플릿 페이지로 응답하기
//		return "error/password";
//	}
	
	
	/*
	 *	RedirectAttribute 객체를 이용하면, 리다일렉트이돈된 페이지에 데이터를 전달 해줄수 있다.  
	 */
	@ExceptionHandler(PasswordException.class)
	public String password(PasswordException pe, RedirectAttributes ra) {
	
		// 리다일렉트 이돈된 페이지에서도 한번 사용할 수 있다.
		// Thymeleaf에서 ${exception} 으로 참조 가능
		ra.addFlashAttribute("exception", pe);
		
		// 리다일렉트 응답을 했으면, model에 담은 내용은 사라진다.
		// 리다일렉트시에도 담은 내용을 사용하고 싶으면, addFlashAttribute를 사용하면 1번 사용 할 수 있다.
		return "redirect:/user/pwd_updateform";
	}
	
	@ExceptionHandler(NotOwnerException.class)
	public String notOwner(NotOwnerException noe, Model model) {
		// 리다일렉트 이돈된 페이지에서도 한번 사용할 숭 있다. 
		// Thymeleaf에서 ${exception}으로 참조가능
		model.addAttribute("exception", noe);
		return "error/info";
	}
}
