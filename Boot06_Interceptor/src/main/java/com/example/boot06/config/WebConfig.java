package com.example.boot06.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.example.boot06.interceptor.LoginInterceptor;

@Configuration
public class WebConfig implements WebMvcConfigurer{
	
	@Autowired
	private LoginInterceptor loginInter;

	// 인터셉터를 레지스트리에 등록할 때 호출되는 메소드를 오버라이드 한다. 
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		// 로그인 인터셉터를 거치치 않을 요청 목록
		String[] whiteList= {"/sub/play", "/cafe/list", "/cafe/detail"};
		
		// 동작을 원하는 인터셉터를 registry 객체를 이용해서 등록을 한다. 
		registry.addInterceptor(loginInter)
			// 인터셉터가 동작할 요청 pattern
			.addPathPatterns("/sub/*", "/cafe/*") 
			// 인터셉터가 동작하지 않는(예외) pattern
			.excludePathPatterns(whiteList);
			//.excludePathPatterns("/sub/play", "/cafe/list", "/cafe/detail");
	}
}
