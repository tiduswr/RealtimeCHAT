package com.tiduswr.rcgateway.config.security;

import java.util.Arrays;
import java.util.List;

import org.apache.hc.core5.http.HttpStatus;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsConfigurationSource;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;

import reactor.core.publisher.Mono;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {

	private final AuthenticationManager reactiveManager;
	private final SecurityContextRepository securityContextRepository;
	private final List<String> CORS_HOSTS;

	public SecurityConfig(@Value("${cors.hosts}") String corsHosts, 
		SecurityContextRepository securityContextRepository,
		AuthenticationManager reactiveManager){
		
			this.securityContextRepository = securityContextRepository;
			this.reactiveManager = reactiveManager;
			this.CORS_HOSTS = Arrays.asList(corsHosts.split(","));	
	}

    @Bean
	SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {

		return http.csrf(csrf -> csrf.disable())
			.cors(cors -> cors.configurationSource(corsConfigurationSource()))
			.exceptionHandling(ex -> 
				ex.authenticationEntryPoint((swe, e) -> 
					Mono.fromRunnable(() -> swe.getResponse().setRawStatusCode(HttpStatus.SC_UNAUTHORIZED))
				).accessDeniedHandler((swe, e) -> 
					Mono.fromRunnable(() -> swe.getResponse().setRawStatusCode(HttpStatus.SC_FORBIDDEN))
				)
			).formLogin(form -> form.disable())
			.httpBasic(basic -> basic.disable())
			.authorizeExchange(auth -> auth
				.pathMatchers("/apis/auth/v1/auth", "/apis/auth/v1/signup", "/apis/auth/v1/refresh_token", 
					"/apis/auth/v1/quit", "/apis/message/v1/ws", "/apis/message/v1/ws/**").permitAll()
                .anyExchange().authenticated()
			).authenticationManager(reactiveManager)
			.securityContextRepository(securityContextRepository).build();
	}

	private CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(CORS_HOSTS);
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setAllowCredentials(true);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

}
