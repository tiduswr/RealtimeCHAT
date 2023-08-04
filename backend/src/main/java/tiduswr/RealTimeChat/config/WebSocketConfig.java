package tiduswr.RealTimeChat.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
import tiduswr.RealTimeChat.exceptions.handlers.StompErrorHandler;
import tiduswr.RealTimeChat.services.JwtService;
import tiduswr.RealTimeChat.services.UserService;

@Configuration
@EnableWebSocketMessageBroker
@Order(Ordered.HIGHEST_PRECEDENCE + 99)
@SuppressWarnings("unused")
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Autowired
    private UserService userService;

    @Autowired
    private JwtService jwtService;

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws")
                .setAllowedOriginPatterns("*")
                .withSockJS();
        registry.setErrorHandler(new StompErrorHandler());
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.setApplicationDestinationPrefixes("/app");
        registry.enableSimpleBroker("/chatroom", "/user", "/queue");
        registry.setUserDestinationPrefix("/user");
    }

    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        registration.interceptors(new WebSocketConnectValidation(jwtService, userService));
    }

}
