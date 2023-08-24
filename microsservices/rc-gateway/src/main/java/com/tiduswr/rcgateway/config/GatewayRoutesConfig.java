package com.tiduswr.rcgateway.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.cloud.gateway.filter.factory.SpringCloudCircuitBreakerFilterFactory;
import org.springframework.cloud.gateway.route.Route;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.Buildable;
import org.springframework.cloud.gateway.route.builder.PredicateSpec;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.cloud.gateway.support.RouteMetadataUtils;

@Configuration
public class GatewayRoutesConfig {

    private static final int GLOBAL_TIMEOUT = 10000;

    @Bean
    RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
        return builder.routes()
            .route("rc-auth-route", r -> buildDeafultPredicateSpec(r, "rc-auth", "auth"))
            .route("rc-user-route", r -> buildDeafultPredicateSpec(r, "rc-user", "user"))
            .route("rc-message-route", r -> buildDeafultPredicateSpec(r, "rc-message", "message"))
            .build();
    }
    
    private Buildable<Route> buildDeafultPredicateSpec(PredicateSpec r, String serviceName, String apiName){
        return r.path(String.format("/apis/%s/v1/**", apiName))
            .filters(f -> f.dedupeResponseHeader("Access-Control-Allow-Origin Access-Control-Allow-Credentials", "RETAIN_UNIQUE") 
                            .rewritePath(String.format("/apis/%s/v1/(?<segment>.*)", apiName), "/$\\{segment}"))          
            .metadata(RouteMetadataUtils.RESPONSE_TIMEOUT_ATTR, GLOBAL_TIMEOUT)
            .metadata(RouteMetadataUtils.CONNECT_TIMEOUT_ATTR, GLOBAL_TIMEOUT)
            .uri(String.format("lb://%s", serviceName.toUpperCase()));
    }

}