package tiduswr.RealTimeChat.config;

import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.core.userdetails.UserDetails;
import tiduswr.RealTimeChat.exceptions.StompAccessorNotFound;
import tiduswr.RealTimeChat.exceptions.StompConnectionRefused;
import tiduswr.RealTimeChat.model.security.JwtTokenType;
import tiduswr.RealTimeChat.model.security.UserPrincipal;
import tiduswr.RealTimeChat.services.JwtService;
import tiduswr.RealTimeChat.services.UserService;

import java.security.Principal;
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
        }
        throw new StompAccessorNotFound("Mensagem inválida");
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
                    String username = jwtService.extractUsername(authToken);
                    UserDetails user = userService.loadUserByUsername(username);

                    if(jwtService.validateToken(authToken, user, JwtTokenType.ACCESS)){
                        accessor.setUser(new UserPrincipal(user));
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