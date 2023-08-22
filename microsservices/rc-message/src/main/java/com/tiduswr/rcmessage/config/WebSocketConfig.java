package com.tiduswr.rcmessage.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.session.Session;
import org.springframework.session.web.socket.config.annotation.AbstractSessionWebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurationSupport;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

import com.tiduswr.rcmessage.exceptions.handlers.StompErrorHandler;
import com.tiduswr.rcmessage.feignclients.JwtService;
import com.tiduswr.rcmessage.feignclients.UserService;

@Configuration
@EnableWebSocketMessageBroker
@SuppressWarnings("unused")
public class WebSocketConfig extends AbstractSessionWebSocketMessageBrokerConfigurer<Session>  {

    private final JwtService jwtService;
    private final WebSocketConnectValidation webSocketConnectValidation;
    
    @Value("${broker.host}")
    private String brokerHost;

    @Value("${broker.port}")
    private int brokerPort;

    @Value("${broker.username}")
    private String brokerUser;

    @Value("${broker.password}")
    private String brokerPass;

    public WebSocketConfig(@Lazy JwtService jwtService) {
        this.jwtService = jwtService;
        this.webSocketConnectValidation = new WebSocketConnectValidation(jwtService);
    }

    @Override
	protected void configureStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws")
                .setAllowedOriginPatterns("*")
                .withSockJS();
        registry.setErrorHandler(new StompErrorHandler());
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.setApplicationDestinationPrefixes("/app");
                
        //registry.enableSimpleBroker("/chatroom", "/user", "/queue");
        //Enable distributed messages between Websocket Instances
        registry.enableStompBrokerRelay("/chatroom", "/user", "/queue")
            .setRelayHost(brokerHost)
            .setRelayPort(brokerPort)
            .setClientLogin(brokerUser)
            .setClientPasscode(brokerPass)
            .setSystemLogin(brokerUser)
            .setSystemPasscode(brokerPass);

        registry.setUserDestinationPrefix("/user");
    }

    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        registration.interceptors(webSocketConnectValidation);
    }

}
