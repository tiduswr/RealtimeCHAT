package com.tiduswr.rcgateway.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity.CsrfSpec;
import org.springframework.security.web.server.SecurityWebFilterChain;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {
    
    @Bean
	SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {

        // TODO: Add security flow

		http
			.authorizeExchange(authorize -> authorize
				.anyExchange().permitAll()
			).csrf(CsrfSpec::disable);

		return http.build();
	}

}
