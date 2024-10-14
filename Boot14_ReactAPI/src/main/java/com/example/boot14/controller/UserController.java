package com.example.boot14.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.boot14.dto.OAuthToken;
import com.example.boot14.dto.UserDto;
import com.example.boot14.exception.PasswordException;
import com.example.boot14.service.UserService;
import com.example.boot14.util.JwtUtil;

import jakarta.servlet.http.HttpServletRequest;

@RestController
public class UserController {

	
	@Autowired
	private JwtUtil jwtUtil;
	
	@Autowired
	private UserService service;
	
	// react js 를 테스트 하기 위한 코딩
	@Autowired
	private AuthenticationManager authManager;
	
	@ResponseBody
	@PostMapping("/auth")
	public String auth(@RequestBody UserDto dto ) throws Exception {
		try {
			//입력한 username 과 password 를 인증토큰 객체에 담아서 
			UsernamePasswordAuthenticationToken authToken=
					new UsernamePasswordAuthenticationToken(dto.getUserName(), dto.getPassword());	
			//인증 메니저 객체를 이용해서 인증을 진행한다 
			authManager.authenticate(authToken);
		}catch(Exception e) {
			//예외가 발생하면 인증실패(아이디 혹은 비밀번호 틀림 등등...)
			e.printStackTrace();
//			throw new Exception("아이디 혹은 비밀번호가 틀려요!");
			throw new PasswordException("아이디 혹은 비밀번호가 틀려요!");

		}
		//예외가 발생하지 않고 여기까지 실행 된다면 인증을 통과 한 것이다. 토큰을 발급해서 응답한다.
		String token=jwtUtil.generateToken(dto.getUserName());
		return "Bearer+"+token;
	}

	// 경로 변수에 전달되는 입력한 userName이 사용가능한지 여부를 json으로 응답하는 메소드 
	@GetMapping("/user/check_username/{userName}")
	public Map<String, Object> checkUserName(@PathVariable("userName") String userName){

		return Map.of("canUse", service.canUse(userName));
	}
	
	// @RequestBody는 axios.post("/user/passsword", {password, newPassword ....})받기위함
	@PatchMapping("/user/password")
	public Map<String, Object> passwordUpdate(@RequestBody UserDto dto){
		service.updatePassword(dto);
		
		return Map.of("isSuccess", true);
	}
	@PatchMapping("/user")
	public Map<String, Object> updateUser(UserDto dto){
		service.updateUser(dto);
		return Map.of("isSuccess", true);
	}
	
	// @RequestBody는 axios.post("/user", {userName:"han", ....}) 뒤에 userName 부분을 받기위함
	@PostMapping("/user")
		public Map<String, Object> addUser(@RequestBody UserDto dto) {
			service.addUser(dto);
			return Map.of("isSuccess", true);
	}
	
	@GetMapping("/user")
	public UserDto getInfo() {
		return service.getInfo();
	}
	
	// 토큰 발급 
	@GetMapping("/api/v1/auth/google/accessTokenCallback")
	public ResponseEntity<String> googleAccessToken(String code, HttpServletRequest request) throws Exception {
		String googleCode = code;
		String googleToken = service.GoogleAccessToken(googleCode);
		return ResponseEntity.status(HttpStatus.OK).body(googleToken);
	//return googleInfo(googleToken);
	}
		@GetMapping("/api/v1/auth/googleLogin")
		public ResponseEntity<String> googleInfo(@RequestHeader("Authorization") String token) {
			String accessToken = token.replace("Bearer ", "");
		    System.out.println("Extracted Access Token: " + accessToken); // 로그로 확인

			OAuthToken oAuthToken = new OAuthToken();
		    oAuthToken.setAccess_token(accessToken);
		    
		    String googleinfo = service.GoogleSignUp(oAuthToken);
		 
		    return ResponseEntity.status(HttpStatus.OK).body(googleinfo);
		}
		
	// 토큰 발급 
	@GetMapping("/api/v1/auth/kakao/accessTokenCallback")
	public ResponseEntity<String> kakaoAccessToken(String code, HttpServletRequest request, OAuthToken oAuthToken) throws Exception {
		String kakaoToken = service.KakaogetAccessToken(request.getParameter("code"));
		return ResponseEntity.status(HttpStatus.OK).body(kakaoToken);
		
	}
	
	// 유저 정보 가져오기
	@GetMapping("/api/v1/auth/kakaoLogin")
	public ResponseEntity<String> kakaoInfo(@RequestHeader("Authorization") String token) {
		String accessToken = token.replace("Bearer ", "");
	    System.out.println("Extracted Access Token: " + accessToken); // 로그로 확인

		OAuthToken oAuthToken = new OAuthToken();
	    oAuthToken.setAccess_token(accessToken);
	    
	    String kakaoInfo = service.KakaoSignUp(oAuthToken);
	 
	    return ResponseEntity.status(HttpStatus.OK).body(kakaoInfo);
	}
	
	@PostMapping("/api/v1/auth/kakaoLogout")
	public ResponseEntity<String> kakaoLogout(@RequestHeader("Authorization") String authHeader, Long  kakaoId) {
		String accessToken = authHeader.replace("Bearer ", "");
		OAuthToken oAuthToken = new OAuthToken();
	    oAuthToken.setAccess_token(accessToken);

		String kakaoLogout=service.kakaoLogout(oAuthToken, kakaoId);
		
		return ResponseEntity.status(HttpStatus.OK).body(kakaoLogout);
	}
}