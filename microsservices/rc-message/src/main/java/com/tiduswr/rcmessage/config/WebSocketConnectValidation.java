package com.tiduswr.rcmessage.config;

import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;

import com.tiduswr.rcmessage.exceptions.StompAccessorNotFound;
import com.tiduswr.rcmessage.exceptions.StompConnectionRefused;
import com.tiduswr.rcmessage.feignclients.JwtService;
import com.tiduswr.rcmessage.model.AccessTokenRequest;
import com.tiduswr.rcmessage.model.UserPrincipal;

import java.security.Principal;
import java.util.List;

@Component
public class WebSocketConnectValidation implements ChannelInterceptor {

    private final JwtService jwtService;

    public WebSocketConnectValidation(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor =
                MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);

        if(accessor != null && accessor.getCommand() != null){
            switch(accessor.getCommand()){
                case CONNECT -> {
                    return validateAuth(message, accessor);
                }case SUBSCRIBE -> {
                    if(accessor.getUser() == null)
                        throw new StompConnectionRefused("Conexão inválida!");

                    return validateSubscription(accessor.getUser(), accessor.getDestination(), message);
                }default -> {
                    if(accessor.getUser() == null)
                        throw new StompConnectionRefused("Conexão inválida!");

                    return message;
                }
            }
        }else if(isAValidHeartbeat(accessor)){
            return message;
        }
        throw new StompAccessorNotFound("Mensagem inválida");
    }

    private boolean isAValidHeartbeat(StompHeaderAccessor accessor){
        final Object SIMP_MESSAGE_TYPE = accessor.getHeader("simpMessageType");
        final boolean USER_IS_NOT_NULL = !(accessor.getUser() == null);

        if(SIMP_MESSAGE_TYPE != null){
            final String EXPECTED_SIMP_MESSAGE_TYPE = "HEARTBEAT";
            final String INCOMING_SIMP_MESSAGE_TYPE = SIMP_MESSAGE_TYPE.toString();

            if(EXPECTED_SIMP_MESSAGE_TYPE.equalsIgnoreCase(INCOMING_SIMP_MESSAGE_TYPE) && USER_IS_NOT_NULL){
                return true;
            }
        }

        return false;
    }

    private Message<?> validateSubscription(Principal user, String destination, Message<?> message) throws StompConnectionRefused{
        final String userName = user.getName();
        List<String> validEndpoints = WebsocketSubscribableEndpoits.privateEndpoint(userName);

        boolean endpointIsValid = validEndpoints
                .stream()
                .anyMatch(endpoint -> endpoint.equalsIgnoreCase(destination));

        if(!endpointIsValid)
            throw new StompConnectionRefused("Endpoint de conexão não permitido para seu usuário!");

        return message;
    }

    private Message<?> validateAuth(Message<?> message, StompHeaderAccessor accessor) throws StompConnectionRefused{
        List<String> authorization = accessor.getNativeHeader("Authorization");
        String authToken = null;

        if(authorization != null){
            authToken = authorization.get(0);

            if(authToken.startsWith("Bearer ")){
                authToken = authToken.substring(7);
                try{
                    String username = jwtService.decodeAndExtractUsername(new AccessTokenRequest(authToken));
                    System.out.println(username + " connected");
                    List<SimpleGrantedAuthority> authorities = List.of(new SimpleGrantedAuthority("USER"));

                    if(jwtService.validateAccessToken(new AccessTokenRequest(authToken), username)){
                        accessor.setUser(new UserPrincipal(
                            new User(username, "", authorities)
                        ));
                        return message;
                    }
                }catch (Exception e){
                    throw new StompConnectionRefused(e.getMessage());
                }
            }
        }
        throw new StompConnectionRefused("Token de acesso inválido ou expirado!");
    }
}