package com.example.boot14;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.example.boot14.dto.UserDto;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;
    
    ObjectMapper oMapper=new ObjectMapper();
    
    @Test
    void authTest() throws Exception {
    	//실제 DB 에 존재하는 정보 
    	UserDto dto=UserDto.builder()
    			.userName("kimgura")
    			.password("@1111")
    			.build();
    	
    	mockMvc.perform(post("/auth")
    			.contentType(MediaType.APPLICATION_JSON)
    			.content(oMapper.writeValueAsString(dto)))
    		.andExpect(status().isOk())
    		.andExpect(jsonPath("$").isNotEmpty())
    		.andDo(result -> {
    			String jwt = result.getResponse().getContentAsString();
    			//문자열이 Bearer 로 시작되는지 확인
//				assertThat(jwt, startsWith("Bearer"));
    			assertTrue(jwt.startsWith("Bearer"));
    			System.out.println(jwt);
    		});
    }
    
    @Test
    void authFailTest() throws Exception {
    	//실제 DB 에 존재하지 않는 정보  
    	UserDto dto=UserDto.builder()
    			.userName("xxx")
    			.password("yyy")
    			.build();
    	
    	mockMvc.perform(post("/auth")
    			.contentType(MediaType.APPLICATION_JSON)
    			.content(oMapper.writeValueAsString(dto)))
    			.andDo(print())
    		.andExpect(status().is4xxClientError()); // 400 번때 에러 응답이 되는지 확인 
    }
    
    // 아이디 사용가능 여부 테스트  ( 존재하는 아이디 => false, 존재하지 않는 아이디 => true)
    @Test
    void checkUserNameTest() throws Exception {
    	//사용 불가능한 아이디
    	String userName1 = "kimgura";
    	mockMvc.perform(get("/user/check_username/"+userName1))
    		.andExpect(status().isOk())
    		.andExpect(jsonPath("$.canUse", is(false)));
    	
    	//사용 가능한 아이디
    	String userName2 = UUID.randomUUID().toString();
    	mockMvc.perform(get("/user/check_username/"+userName2))
		.andExpect(status().isOk())
		.andExpect(jsonPath("$.canUse", is(true)));
    	
    }
}



















