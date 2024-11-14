package cherrysumer.cherrysumer.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
@RequiredArgsConstructor
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {
    private final StompHandler stompHandler;
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {

        // socketJs 클라이언트가 WebSocket 핸드셰이크를 하기 위해 연결할 endpoint를 지정할 수 있다.
        registry.addEndpoint("/chat/inbox")
                .setAllowedOriginPatterns("*") // cors 허용을 위해 꼭 설정해주어야 한다.
                .withSockJS();
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        // 메모리 기반 메시지 브로커가 해당 api를 구독하고 있는 클라이언트에게 메시지를 전달한다.
        // to subscriber
        registry.enableSimpleBroker("/sub");

        // 클라이언트로부터 메시지를 받을 api의 prefix를 설정한다.
        // publish
        registry.setApplicationDestinationPrefixes("/pub");


    }

    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        registration.interceptors(stompHandler);
    }
}
