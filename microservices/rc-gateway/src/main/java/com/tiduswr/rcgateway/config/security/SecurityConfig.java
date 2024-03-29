package com.tiduswr.rcgateway.config.security;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
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
	private final String[] PUBLIC_ENDPOINTS;
	
	public SecurityConfig(@Value("${cors.hosts}") String corsHosts, 
		SecurityContextRepository securityContextRepository,
		AuthenticationManager reactiveManager){
		
			this.securityContextRepository = securityContextRepository;
			this.reactiveManager = reactiveManager;
			this.CORS_HOSTS = Arrays.asList(corsHosts.split(","));	
			this.PUBLIC_ENDPOINTS = new String[]{
				"/apis/auth/v1/auth", 
				"/apis/auth/v1/signup", 
				"/apis/auth/v1/refresh_token", 
				"/apis/auth/v1/quit", 
				"/apis/auth/v1/recover_password",
				"/apis/auth/v1/recover_password/validate/**",
				"/apis/message/v1/ws", 
				"/apis/message/v1/ws/**",
				"/actuator/health/readiness"
			};
	}

    @Bean
	SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {

		return http.csrf(csrf -> csrf.disable())
			.cors(cors -> cors.configurationSource(corsConfigurationSource()))
			.exceptionHandling(ex -> 
				ex.authenticationEntryPoint((swe, e) -> 
					Mono.fromRunnable(() -> swe.getResponse().setRawStatusCode(HttpStatus.FORBIDDEN.value()))
				).accessDeniedHandler((swe, e) -> 
					Mono.fromRunnable(() -> swe.getResponse().setRawStatusCode(HttpStatus.FORBIDDEN.value()))
				)
			).formLogin(form -> form.disable())
			.httpBasic(basic -> basic.disable())
			.authorizeExchange(auth -> auth
				.pathMatchers(PUBLIC_ENDPOINTS).permitAll()
				.pathMatchers(".*internal.*").denyAll()
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
