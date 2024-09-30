package com.example.boot11.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;

import com.example.boot11.dto.KakaoProfile;
import com.example.boot11.dto.OAuthToken;
import com.example.boot11.dto.UserDto;
import com.example.boot11.service.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.http.HttpSession;

@Controller
public class UserController {
	@Autowired 
	private UserService service; 

	@PostMapping("/user/update")
	public String update(UserDto dto) {
		service.updateUser(dto);
		//개인 정보 보기로 다시 리다일렉트 시킨다 
		return "redirect:/user/info";
	}
	
	@GetMapping("/user/updateform")
	public String updateForm(Model model) {
		//Model 에 UserDto 가 담기도록 서비스 메소드에 전달한다 
		service.getInfo(model);
		
		return "user/updateform";
	}
	
	@PostMapping("/user/pwd_update")
	public String pwdUpdate(UserDto dto, HttpSession session) {
		// 비밀번호 수정 이후
		service.updatePassword(dto);
		// 강제 로그아웃 처리
		session.invalidate();
		return "user/pwd_update";
	}
	
	@GetMapping("/user/pwd_updateform")
	public String pwdUpdateForm() {
		return "user/pwd_updateform";
	}
	// 개인정보 보기 요청 처리
	@GetMapping("/user/info")
	public String info(Model model) {
		// service 객체 Model을 전달해서 Model 에 UserDto가 담도록한다.
		service.getInfo(model);
		return "user/info";
	}
	// 회원가입 요청처리
	@PostMapping("/user/signup")
	public String signup(UserDto dto) {
		// dto에는 userName, password, email이 들어있다. 
		service.addUser(dto);
		return "user/signup";
		
	}
	// 회원가입 폼 요청 처리
	@GetMapping("/user/signup_form")
	public String signupForm() {
		return "user/signup_form";
	}
	// 세션 허용 수량 초과시 
	@GetMapping("/user/expired")
	public String userExpired() {
		return "user/expired";
	}
	//권한 부족시 or 403 인 경우 
	@RequestMapping("/user/denied")
	public String userDenied() {
		return "user/denied";
	}
	
	@GetMapping("/user/loginform")
	public String loginform() {
		return "user/loginform";
	}
	
	// 로그인이 필요한 요청 경로를 로그인 하지 않은 상태로 요청하면 redirect 되는 요청경로 
	@GetMapping("/user/required_loginform")
	public String required_loginform() {
		return "user/required_loginform";
	}

	// /user/login 요청 후 로그인 성공인경우 forward 이동 될 url
	@PostMapping("/user/login_success")
	public String loginSuccess() {
		return "user/login_success";
	}
	
	// 로그인 폼을 제출(post) 한 로그인 프로세스 중에 forward 되는 경로이기 때문에, @PostMapping 임에 주의!!
	@PostMapping("/user/login_fail")
	public String loginFail() {
		// 로그인 실패임을 알릴 페이지
		return "user/login_fail";
	}
	
	//@ResponseBody
	@GetMapping("/callback")
	public String kakaoLogin(String code, HttpSession session) {
		
		// 1. 인가 코드 
		RestTemplate rt = new RestTemplate();
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");
		
		MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
		params.add("grant_type", "authorization_code");
		params.add("client_id", "ea3fc29935a7e7b17c9a328b221b9488");
		params.add("redirect_uri", "http://localhost:8888/callback");
		params.add("code", code);
				
		HttpEntity<MultiValueMap<String, String>> kakaoTokenRequest = 
				new HttpEntity<>(params, headers);
		
		ResponseEntity<String> response = rt.exchange(
				"https://kauth.kakao.com/oauth/token",
				HttpMethod.POST,
				kakaoTokenRequest,
				String.class
		);
		
		// 2. 액세스 토큰 
		ObjectMapper objectMapper = new ObjectMapper();
		OAuthToken oauthToken = null;
		try {
		    oauthToken = objectMapper.readValue(response.getBody(), OAuthToken.class);
		} catch (JsonMappingException e) {
		    e.printStackTrace();
		} catch (JsonProcessingException e) {
		    e.printStackTrace();
		}
		System.out.println("카카오 액세스 토큰 : " + oauthToken.getAccess_token());
		 
		
		RestTemplate rt2 = new RestTemplate();

		// HttpHeader 오브젝트 생성
		HttpHeaders headers2 = new HttpHeaders();
		headers2.add("Authorization", "Bearer " + oauthToken.getAccess_token());
		headers2.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8"); // 내가 지금 전송할 body data 가
		                                                                                    // key=velue 형임을 명시

		// HttpHeader 와 HttpBody를 하나의 오브젝트에 담기
		HttpEntity<MultiValueMap<String, String>> kakaoProfileRequest = new HttpEntity<>(headers2);

		// Http 요청하기 - Post 방식으로 - 그리고 Response 변수의 응답 받음.
		ResponseEntity<String> response2 = rt2.exchange("https://kapi.kakao.com/v2/user/me", HttpMethod.POST,
		        kakaoProfileRequest, String.class // String 타입으로 응답 데이터를 받겠다.
		);

		System.out.println("유저정보 : " + response2.getBody());
		
		// Gson, Json, Simple, ObjectMapper
				ObjectMapper objectMapper2 = new ObjectMapper();
				KakaoProfile kakaoProfile = null;
				try {
					kakaoProfile = objectMapper2.readValue(response2.getBody(), KakaoProfile.class);
				} catch (JsonMappingException e) {
					e.printStackTrace();
				} catch (JsonProcessingException e) {
					e.printStackTrace();
				}
				
				// User 오브젝트 : userName, password, email
				System.out.println("카카오 아이디(번호) : " + kakaoProfile.getId());
				System.out.println("카카오 이메일 : " + kakaoProfile.getKakao_account().getEmail());
				
				System.out.println("카카오 토큰 요청에 대한 응답 : "+response+" / 인증 코드 값 : "+code+response.getBody());
				
		return "카카오 토큰 요청 완료 : 토큰 요청에 대한 응답 :"+response+" / 응답 인증 코드 : "+code;
	}
	
}
