package com.example.boot14.config;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.*;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {
	 @Override
	    public void registerStompEndpoints(StompEndpointRegistry registry) {
	        // 클라이언트가 WebSocket에 연결할 엔드포인트 설정
	        registry.addEndpoint("/api/ws")
	        			  .setAllowedOrigins("")
	        			  .withSockJS();
	    }

	    @Override
	    public void configureMessageBroker(MessageBrokerRegistry registry) {
	        // 메시지 브로커 설정
	        registry.enableSimpleBroker("/topic"); // 메시지를 브로드캐스트할 경로
	        registry.setApplicationDestinationPrefixes("/app"); // 애플리케이션에서 메시지를 보낼 때 사용할 접두사
	    }
}
