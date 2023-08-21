package com.tiduswr.rcgateway.config.security;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import com.tiduswr.rcgateway.services.JwtService;

import reactor.core.publisher.Mono;

@Component
public class AuthenticationManager implements ReactiveAuthenticationManager {

    @Autowired
    private JwtService jwtService;

    @Override
    public Mono<Authentication> authenticate(Authentication authentication) {
        try{
            String authToken = authentication.getCredentials().toString();
            Optional<String> username = jwtService.extractUsername(authToken);
            
            if(!username.isPresent()) 
                return Mono.empty();

            final boolean TOKEN_IS_VALID = jwtService.validateToken(authToken);

            return Mono.just(TOKEN_IS_VALID)
                .filter(valid -> valid)
                .switchIfEmpty(Mono.empty())
                .map(valid -> new UsernamePasswordAuthenticationToken(
                    username,
                    authentication.getCredentials(),
                    List.of(new SimpleGrantedAuthority("USER"))
                ));
        }catch(Exception e){
            return Mono.empty();
        }
    }
    
}
