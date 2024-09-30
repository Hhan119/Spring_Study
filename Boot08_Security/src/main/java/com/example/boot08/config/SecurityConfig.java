package com.example.boot08.config;

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

// 설정 클래스라고 알려준다.
@Configuration 
// Security를 설정하기 위한 어노테이션
@EnableWebSecurity
public class SecurityConfig{ 
	/*
	 * 	매개변수에 전달되는 HttpSecurity 객체를 이용해서 우리의 프로젝트 상황에 맞는 설정을 기반으로,
	 * 	만들어진 SecurityFilterChain 객체를 리턴해주어야 한다.
	 * 	즉, SecurityFilterChain 객체도 스프링이 관리하는 Bean이 되어야 한다.  
	 */
	// 메소드에서 리턴되는 securityFilterChain을 bean으로 만들어준다. 
	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception{
		String[] whiteList = {"/", "/play", "/user/loginform", "/user/login_fail", "/user/expired"};
		
		httpSecurity.csrf(csrf->csrf.disable())
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
	
	// 비밀번호를 암호화해주는 객체를 bean으로 만든다.
	@Bean
	PasswordEncoder passwordEncoder() {
		// 여기서 리턴해주는 객체도 bean으로 된다.
		return new BCryptPasswordEncoder();
	}
	// 인증 매니저 객체를 bean으로 만든다. (Spring Security가 자동으로 처리 할때도 사용되는 객체)
	@Bean
	AuthenticationManager authenticationManager(HttpSecurity http,
			BCryptPasswordEncoder encoder, UserDetailsService service) throws Exception{
		// 적적한 설정을 한 인증 매니저 객체를 리턴해주면 bean이 되어서 Spring Security가 사용한다.
		return http.getSharedObject(AuthenticationManagerBuilder.class)
				.userDetailsService(service)
				.passwordEncoder(encoder)
				.and()
				.build();
	}

}
