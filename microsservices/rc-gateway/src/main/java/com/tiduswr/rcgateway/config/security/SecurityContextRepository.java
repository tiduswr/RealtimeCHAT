package com.tiduswr.rcgateway.config.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.web.server.context.ServerSecurityContextRepository;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.security.core.context.SecurityContextImpl;

import reactor.core.publisher.Mono;

@Component
public class SecurityContextRepository implements ServerSecurityContextRepository {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Override
    public Mono<Void> save(ServerWebExchange swe, SecurityContext sc) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Mono<SecurityContext> load(ServerWebExchange swe) {
        return Mono.justOrEmpty(swe.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION))
            .filter(authHeader -> authHeader.startsWith("Bearer "))
            .flatMap(authHeader -> {
                String authToken = authHeader.substring(7);
                JwtAuth auth = new JwtAuth(authToken);
                return this.authenticationManager.authenticate(auth).map(SecurityContextImpl::new);
            });
    }
}