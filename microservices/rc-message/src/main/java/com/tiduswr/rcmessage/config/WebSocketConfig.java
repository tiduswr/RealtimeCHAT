package com.tiduswr.rcmessage.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.handler.ExceptionWebSocketHandlerDecorator;

import com.tiduswr.rcmessage.exceptions.handlers.StompErrorHandler;
import com.tiduswr.rcmessage.feignclients.JwtService;

@Configuration
@EnableWebSocket
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer{

    private final WebSocketConnectValidation webSocketConnectValidation;
    
    @Value("${spring.rabbitmq.host}")
    private String brokerHost;

    @Value("${spring.rabbitmq.port}")
    private int brokerPort;

    @Value("${spring.rabbitmq.username}")
    private String brokerUser;

    @Value("${spring.rabbitmq.password}")
    private String brokerPass;

    public WebSocketConfig(@Lazy JwtService jwtService) {
        this.webSocketConnectValidation = new WebSocketConnectValidation(jwtService);
    }

    @Override
	public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws")
                .setAllowedOriginPatterns("*")
                .withSockJS();
        registry.setErrorHandler(new StompErrorHandler());
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.setApplicationDestinationPrefixes("/app")
            .enableStompBrokerRelay("/topic")
            .setRelayHost(brokerHost)
            .setRelayPort(brokerPort)
            .setClientLogin(brokerUser)
            .setClientPasscode(brokerPass)
            .setSystemLogin(brokerUser)
            .setSystemPasscode(brokerPass);
    }

    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        registration.interceptors(webSocketConnectValidation);
    }

}
