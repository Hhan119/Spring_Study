package com.example.boot14.service;

import java.io.File;
import java.util.UUID;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import com.example.boot14.dto.GoogleProfile;
import com.example.boot14.dto.KakaoProfile;
import com.example.boot14.dto.OAuthToken;
import com.example.boot14.dto.UserDto;
import com.example.boot14.entity.User;
import com.example.boot14.exception.PasswordException;
import com.example.boot14.repository.UserDao;
import com.example.boot14.repository.UserRepository;
import com.example.boot14.util.JwtUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

@Service
public class UserServiceImpl implements UserService {
	@Value("${file.location}")
	private String fileLocation;

	@Autowired
	private JwtUtil jwtUtil;

	@Autowired
	private AuthenticationManager authManager;

	@Autowired
	private UserDao dao;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private PasswordEncoder encoder;

	@Autowired
	private UserRepository repo;

	@Value("${google.client_id}")
	private String g_client_id;

	@Value("${google.client_pw}")
	private String g_client_pw;

	@Value("${google.redirect_uri}")
	private String g_redirect_uri;

	@Value("${kakaopassword.key}")
	private String kakaopassword;

	@Value("${kakao.client_id}")
	private String client_id;

	@Value("${kakao.redirect_uri}")
	private String redirect_url;

	// 회원 찾기
	@Override
	public User KakaoFindId(String username) {
		User user = repo.findByUserName(username);
		return user;
	}

	// 로그인 및 회원 DB 저장
	@Override
	public String KakaoSignUp(OAuthToken kakaoToken) {
		// 1. Kakao API를 통해 사용자 정보 요청
		RestTemplate rt2 = new RestTemplate();
		HttpHeaders headers2 = new HttpHeaders();
		headers2.add("Authorization", "Bearer " + kakaoToken.getAccess_token());
		headers2.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8"); // 내가 지금 전송할 body data 가

		HttpEntity<MultiValueMap<String, String>> kakaoProfileRequest = new HttpEntity<>(headers2);

		ResponseEntity<String> response2 = rt2.exchange("https://kapi.kakao.com/v2/user/me", HttpMethod.POST,
				kakaoProfileRequest, String.class // String 타입으로 응답 데이터를 받겠다.
		);

		System.out.println("유저정보 : " + response2.getBody());

		// 2. 응답된 JSON을 KakaoProfile 객체로 변환
		ObjectMapper objectMapper2 = new ObjectMapper();
		objectMapper2.registerModule(new JavaTimeModule());
		objectMapper2.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		objectMapper2.setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE);
		KakaoProfile kakaoProfile = null;
		try {
			kakaoProfile = objectMapper2.readValue(response2.getBody(), KakaoProfile.class);
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}

		// 3. 유저 정보 생성
		User kakaoUser = User.builder()
				.userName(kakaoProfile.getId() + "_" + kakaoProfile.getKakao_account().getEmail())
				.password(encoder.encode(kakaopassword)).email(kakaoProfile.getKakao_account().getEmail()).build();

		// 4. 데이터베이스에서 유저 조회
		User originUser = repo.findByUserName(kakaoUser.getUserName());
		if (originUser != null) {
			System.out.println("이미 존재하는 유저입니다.");
		} else {
			// 5. 유저가 없을 경우 저장
			repo.save(kakaoUser);
			System.out.println("새로운 유저가 저장되었습니다.");
		}

		// 6. 로그인 처리
		// Authentication authentication = authManager.authenticate(new
		// UsernamePasswordAuthenticationToken(kakaoUser.getUsername(), kakaopassword));
		// SecurityContextHolder.getContext().setAuthentication(authentication);

		// 7. 카카오 유저 정보를 JSON으로 반환
		JSONObject kakaoInfo2 = new JSONObject();
		kakaoInfo2.put("id", kakaoProfile.getId());
		kakaoInfo2.put("nickname", kakaoProfile.getKakao_account().getProfile().getNickname());
		kakaoInfo2.put("profile_image", kakaoProfile.getKakao_account().getProfile().getProfile_image_url());
		kakaoInfo2.put("email", kakaoProfile.getKakao_account().getEmail());
		kakaoInfo2.put("kakaoToken", kakaoToken.getAccess_token());
		kakaoInfo2.put("kakaoRefreshToken", kakaoToken.getRefresh_token());

		System.out.println("kakaoInfo2: " + kakaoInfo2.toString()); // 보기 좋게 출력

		UserDto dto = new UserDto();
		dto.setRole(kakaoProfile.getKakao_account().getEmail());

		return kakaoInfo2.toString();

	}

	// 액세스 토큰 발급
	@Override
	public String KakaogetAccessToken(String code) {
		RestTemplate rt = new RestTemplate();

		// HttpHeader 오브젝트 생성
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

		// HttpBody 오브젝트 생성
		MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
		params.add("grant_type", "authorization_code");
		params.add("client_id", client_id);
		// params.add("redirect_url", redirect_url);
		params.add("code", code);
		System.out.println(code);

		// HttpHeader와 HttpBody를 하나의 오브젝트에 담기
		HttpEntity<MultiValueMap<String, String>> kakaoTokenRequest = new HttpEntity<>(params, headers);

		// Http 요청하기 - Post방식으로 - 그리고 response 변수의 응답 받음.
		ResponseEntity<String> response = rt.exchange("https://kauth.kakao.com/oauth/token", HttpMethod.POST,
				kakaoTokenRequest, String.class);
		// Gson, Json Simple, ObjectMapper
		ObjectMapper objectMapper = new ObjectMapper();
		OAuthToken oauthToken = null;
		try {
			oauthToken = objectMapper.readValue(response.getBody(), OAuthToken.class);
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		System.out.println("카카오 엑세스 토큰 : " + oauthToken.getAccess_token());
//			RestTemplate rt2 = new RestTemplate();
//			HttpHeaders headers2 = new HttpHeaders();
//			headers2.add("Authorization", "Bearer " + oauthToken.getAccess_token());
//			headers2.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8"); // 내가 지금 전송할 body data 가
//			                                                                                    // key=velue 형임을 명시
//
//			// HttpHeader 와 HttpBody를 하나의 오브젝트에 담기
//			
//			HttpEntity<MultiValueMap<String, String>> kakaoProfileRequest = 
//					new HttpEntity<>(headers2);
//
//			// Http 요청하기 - Post 방식으로 - 그리고 Response 변수의 응답 받음.
//			ResponseEntity<String> response2 = rt2.exchange(
//					"https://kapi.kakao.com/v2/user/me",
//					HttpMethod.POST,
//					kakaoProfileRequest,
//			        String.class // String 타입으로 응답 데이터를 받겠다.
//			);
		// return KakaoSignUp(oauthToken);
		// return "Access_Token : Bearer+" +oauthToken.getAccess_token()+" Reflash_Token
		// : "+oauthToken.getRefresh_token();
		return "Bearer " + oauthToken.getAccess_token();
	}

	@Override
	public String kakaoLogout(OAuthToken oAuthToken, Long kakaoId) {
		// RestTemplate를 사용해 로그아웃 요청
		RestTemplate rt = new RestTemplate();
		HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", "Bearer " + oAuthToken.getAccess_token());
		headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

		// HttpBody 오브젝트 생성
		MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
		params.add("target_id_type", "user_id");
		params.add("target_id", kakaoId.toString());

		HttpEntity<MultiValueMap<String, String>> kakaoLogoutRequest = new HttpEntity<>(headers);
		// 로그아웃 요청 (POST)
		ResponseEntity<String> response = rt.exchange("https://kapi.kakao.com/v1/user/logout", HttpMethod.POST,
				kakaoLogoutRequest, String.class);
		System.out.println(response.getBody());
		// 응답 확인
		if (response.getStatusCode().is2xxSuccessful()) {
			System.out.println("카카오 로그아웃 성공");
			return "카카오 로그아웃 성공";
		} else {
			System.out.println("카카오 로그아웃 실패");
			return "카카오 로그아웃 실패";
		}
	}

	// 구글 액세스 토큰 발급
	@Override
	public String GoogleAccessToken(String code) {
		System.out.println(code);
//			String decod=code.replace("/", "%2F");
//			System.out.println("서비스 인가코드 체크 : "+decode);

		RestTemplate rt = new RestTemplate();
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-type", "application/x-www-form-urlencoded");
		// HttpBody 오브젝트 생성
		MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
		params.add("code", code);
		params.add("client_id", g_client_id);
		params.add("client_secret", g_client_pw);
		params.add("redirect_uri", g_redirect_uri);
		params.add("grant_type", "authorization_code");

		// HttpHeader와 HttpBody를 하나의 오브젝트에 담기
		HttpEntity<MultiValueMap<String, String>> googleTokenRequest = new HttpEntity<>(params, headers);
		System.out.println(params);

		// Http 요청하기 - Post방식으로 - 그리고 response 변수의 응답 받음.
		ResponseEntity<String> response = rt.exchange("https://oauth2.googleapis.com/token", HttpMethod.POST,
				googleTokenRequest, String.class);

		System.out.println(response.getBody());

		ObjectMapper objectMapper = new ObjectMapper();
		OAuthToken oauthToken = null;

		try {
			oauthToken = objectMapper.readValue(response.getBody(), OAuthToken.class);
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		System.out.println("구글 엑세스 토큰 : " + oauthToken.getAccess_token());
		// return GoogleSignUp(oauthToken);
		// return "Access_Token : Bearer+" +oauthToken.getAccess_token()+" Reflash_Token
		// : "+oauthToken.getRefresh_token();
		return "Bearer " + oauthToken.getAccess_token();
	}

	@Override
	public String GoogleSignUp(OAuthToken googleToken) {
		// 1. Kakao API를 통해 사용자 정보 요청
		RestTemplate rt2 = new RestTemplate();
		HttpHeaders headers2 = new HttpHeaders();
		headers2.add("Authorization", "Bearer " + googleToken.getAccess_token());
		headers2.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8"); // 내가 지금 전송할 body data 가

		HttpEntity<MultiValueMap<String, String>> googleProfileRequest = new HttpEntity<>(headers2);

		ResponseEntity<String> response2 = rt2.exchange("https://www.googleapis.com/userinfo/v2/me", HttpMethod.GET,
				googleProfileRequest, String.class // String 타입으로 응답 데이터를 받겠다.
		);

		System.out.println("유저정보 : " + response2.getBody());

		// 2. 응답된 JSON을 KakaoProfile 객체로 변환
		ObjectMapper objectMapper2 = new ObjectMapper();
		objectMapper2.registerModule(new JavaTimeModule());
		objectMapper2.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		objectMapper2.setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE);
		GoogleProfile googleProfile = null;
		try {
			googleProfile = objectMapper2.readValue(response2.getBody(), GoogleProfile.class);
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}

		// 3. 유저 정보 생성
		User googleUser = User.builder().userName(googleProfile.getSub() + "_" + googleProfile.getEmail())
				.password(encoder.encode(kakaopassword)).email(googleProfile.getEmail()).build();

		// 4. 데이터베이스에서 유저 조회
		User originUser = repo.findByUserName(googleUser.getUserName());
		if (originUser != null) {
			System.out.println("이미 존재하는 유저입니다.");
		} else {
			// 5. 유저가 없을 경우 저장
			repo.save(googleUser);
			System.out.println("새로운 유저가 저장되었습니다.");
		}

		// 6. 로그인 처리
//								Authentication authentication = authManager.authenticate(new UsernamePasswordAuthenticationToken(googleUser.getUsername(), kakaopassword));
//								SecurityContextHolder.getContext().setAuthentication(authentication);

		// 7. 구글 유저 정보를 JSON으로 반환
		JSONObject googleInfo2 = new JSONObject();
		googleInfo2.put("id", googleProfile.getSub());
		googleInfo2.put("name", googleProfile.getName());
		googleInfo2.put("nickname", googleProfile.getGivenName());
		googleInfo2.put("picture", googleProfile.getPicture());
		googleInfo2.put("email", googleProfile.getEmail());
		googleInfo2.put("googleToken", googleToken.getAccess_token());
		googleInfo2.put("kakaoRefreshToken", googleToken.getRefresh_token());

		System.out.println("googleInfo2: " + googleInfo2.toString()); // 보기 좋게 출력

		return googleInfo2.toString();
	}

	@Override
	public String GoogleLogout(OAuthToken oAuthToken) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void addUser(UserDto dto) {
		// role을 일반 USER로 넣어준다.
		dto.setRole("USER");
		// 비밀번호를 암호화해서 dto에 다시 넣기
		String encoderPwd = encoder.encode(dto.getPassword());
		dto.setPassword(encoderPwd);

		dao.insert(dto);

	}

	@Override
	public UserDto getInfo() {
		// 개인 정보를 본다는 것은 이미 로그인을 한 상태인데 로그인된 userName은 어떻게 얻어내지?
		// Spring Security의 기능을 통해서 얻어낸다.
		String userName = SecurityContextHolder.getContext().getAuthentication().getName();

		return dao.getData(userName);
	}

	@Override
	public void updateUser(UserDto dto) {
		MultipartFile image = dto.getImage();
		// 만일 선택한 프로필 이미지가 있다면
		if (image.getSize() != 0) {
			// 파일을 원하는 위치로 이동시켜 놓고
			String saveFileName = UUID.randomUUID().toString();
			// 저장할 파일의 전체 경로 구성하기
			String filePath = fileLocation + File.separator + saveFileName;
			try {
				// 업로드된 파일을 이동시킬 목적지 File 객체
				File f = new File(filePath);
				// MultipartFile 객체의 메소드를 통해서 실제로 이동시키기(전송하기)
				dto.getImage().transferTo(f);
			} catch (Exception e) {
				e.printStackTrace();
			}
			// UserDto 에 저장된 이미지의 이름을 넣어준다.
			dto.setProfile(saveFileName);
		}
		// 로그인된 userName 도 dto 에 담아준다
		String userName = SecurityContextHolder.getContext().getAuthentication().getName();
		dto.setUserName(userName);

		// dao 를 이용해서 수정반영한다
		dao.update(dto);
	}

	@Override
	public void updatePassword(UserDto dto) {
		// 1. 로그인 된 userName을 얻어낸다.
		String userName = SecurityContextHolder.getContext().getAuthentication().getName();
		// 2. 기존의 비밀번호를 DB에서 읽어와서 (암호화된 비밀번호)
		String encoderPwd = dao.getData(userName).getPassword();
		// 3. 입력한(암호화 되지 않은 구비밀번호) 와 일치하는지 비교해서
		// .checkpw(암호화 되지 않은 비밀번호, 암호화된 비밀번호
		boolean isValid = BCrypt.checkpw(dto.getPassword(), encoderPwd);
		// 4. 만약에 일치하지 않으면, Exception을 발생시킨다.
		if(!isValid) {
//			throw new RuntimeException("비밀 번호가 일치하지 않아요");
			// 이건 직접 만든 예외처리 
			throw new PasswordException("비밀 번호가 일치하지 않아요!");
		}
		// 5. 일치하면 새비밀번호를 암호화해서 dto에 담은 다음
		dto.setNewPassword(encoder.encode(dto.getNewPassword()));
		// 6. userName도 Dto에 담고
		dto.setUserName(userName);
		// 7. DB에 비밀번호 수정 반영을 한다.
		dao.updatePwd(dto);
	}

	@Override
	public boolean canUse(String userName) {
		// userName을 이용해서 UserDto를 읽어와본다.
		// 데이터가 없으면 null이고, null이면 사용가능한 상태이다.
		UserDto dto = dao.getData(userName);
		// 사용 가능한지 여부
		boolean canUse = dto == null;

		return canUse;
	}
}
