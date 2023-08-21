package com.tiduswr.rcmessage.feignclients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.tiduswr.rcmessage.model.AccessTokenRequest;

@Component
@FeignClient(contextId = "jwt-service", name = "rc-auth", path="/internal/decode")
public interface JwtService {
    
    @PostMapping("/username")
    String decodeAndExtractUsername(@RequestBody AccessTokenRequest accessToken);


    @PostMapping("/validby/{username}")
    boolean validateAccessToken(@RequestBody AccessTokenRequest accessToken, @PathVariable String username);
    
}
