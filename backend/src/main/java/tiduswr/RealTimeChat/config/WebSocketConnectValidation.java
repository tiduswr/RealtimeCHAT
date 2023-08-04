package tiduswr.RealTimeChat.config;

import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.core.userdetails.UserDetails;
import tiduswr.RealTimeChat.exceptions.StompConnectionRefused;
import tiduswr.RealTimeChat.exceptions.UnauthorizedException;
import tiduswr.RealTimeChat.model.security.JwtTokenType;
import tiduswr.RealTimeChat.model.security.UserPrincipal;
import tiduswr.RealTimeChat.services.JwtService;
import tiduswr.RealTimeChat.services.UserService;

import java.util.List;

public class WebSocketConnectValidation implements ChannelInterceptor {

    private final JwtService jwtService;
    private final UserService userService;

    public WebSocketConnectValidation(JwtService jwtService, UserService userService){
        this.jwtService = jwtService;
        this.userService = userService;
    }

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor =
                MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);

        if (accessor != null && StompCommand.CONNECT.equals(accessor.getCommand())) {
            List<String> authorization = accessor.getNativeHeader("Authorization");
            String authToken = null;

            if(authorization != null){
                authToken = authorization.get(0);

                if(authToken.startsWith("Bearer ")){
                    authToken = authToken.substring(7);
                    try{
                        String username = jwtService.extractUsername(authToken);
                        UserDetails user = userService.loadUserByUsername(username);

                        if(jwtService.validateToken(authToken, user, JwtTokenType.ACCESS)){
                            accessor.setUser(new UserPrincipal(user));
                            return message;
                        }
                    }catch (Exception e){
                        System.out.println(e.getMessage());
                    }
                }
            }
            throw new StompConnectionRefused("Erro: Token de acesso inválido ou expirado!");
        }
        return message;
    }
}