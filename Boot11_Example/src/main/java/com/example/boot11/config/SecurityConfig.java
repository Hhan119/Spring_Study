package com.example.boot11.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.header.Header;

@EnableWebSecurity
@Configuration
public class SecurityConfig {

	// 비밀번호를 암호화 해주는 객체를 bean으로 만든다.(스프링 security 가 사용한다.)
	@Bean
	PasswordEncoder passwordEncoder() {
		// 여기서 리턴해주는 객체도 bean으로 된다.
		return new BCryptPasswordEncoder();
	}
	
	/*
	 * 	매개변수에 전달되는 HttpSecurity 객체를 이용해서 우리의 프로젝트 상황에 맞는 설정을 기반으로,
	 * 	만들어진 SecurityFilterChain 객체를 리턴해주어야 한다.
	 * 	즉, SecurityFilterChain 객체도 스프링이 관리하는 Bean이 되어야 한다.  
	 */
	// 메소드에서 리턴되는 securityFilterChain을 bean으로 만들어준다. 
	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception{
		String[] whiteList = {"/", "/user/loginform", "/user/login_fail", "/user/expired",
				"/user/signup_form", "/user/signup", "/error", "/upload/images/*",
				"/file/list", "/file/download", "/cafe/list", "/cafe/detail", "/editor/images/*", "/cafe/comment_list", "/gallery/list",
				"/callback"};
		
		httpSecurity
					.headers(header ->
						// 동일한 origin에서 iframe을 사용할 수 있도록 설정(default 값은 사용불가)
						header.frameOptions(option->option.sameOrigin()) // SmartEditor에서 필요함
						
					)
					.csrf(csrf->csrf.disable())
					.authorizeHttpRequests(config->
					config
						.requestMatchers(whiteList).permitAll()
						.requestMatchers("/admin/**").hasRole("ADMIN")
						.requestMatchers("/staff/**").hasAnyRole("ADMIN", "STAFF")
						.anyRequest().authenticated()
						)
					.formLogin(config->
					config
						// 인증을 거치치 않은 사용자를 redirect 시킬 경로 설정
						.loginPage("/user/required_loginform")
						// 로그인 처리를 할 때 요청 될 url 설정(spring security가 login 처리를 대신 해줌)
						 .loginProcessingUrl("/user/login")
						// 로그인 처리를 대신 하려면 어떤 파라미터명으로 username과 password가 넘어오는지 알려주기
						 .usernameParameter("userName")
						 .passwordParameter("password")
						// 로그인 성공시 forward 될 url 경로
						 //.successForwardUrl("/user/login_success")
						 .successHandler(new AuthSuccessHandler())
						 .failureForwardUrl("/user/login_fail")
						//위에 명시한 모든 요청경로를 로그인 없이 요청할 수 있도록 설정
						 .permitAll() 
					)
					.logout(config->
						config
						// Spring Security가 자동으로 로그아웃 처리 해줄 경로 설정
						.logoutUrl("/user/logout")
						// 로그 아웃 이후에 redirect 시킬 경로 설정 
						.logoutSuccessUrl("/")
						.permitAll()
					)
					.exceptionHandling(config ->
						//403 forbidden인 경우 forward 이동 시킬 경로 설정
						config.accessDeniedPage("/user/denied")
					)
					.sessionManagement(config ->
						config
						// 최대 허용 세션 갯수
						.maximumSessions(1)
						// 허용 세션 갯수가 넘어서 로그인 해제된 경우 리디렉션 시킬 경로 
						.expiredUrl("/user/expired")
					);
		return httpSecurity.build();
	}
	
}
