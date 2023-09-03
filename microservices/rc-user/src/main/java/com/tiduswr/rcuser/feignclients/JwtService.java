package com.tiduswr.rcuser.feignclients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.tiduswr.rcuser.model.AccessTokenRequest;

@Component
@FeignClient(contextId = "jwt-service", name = "${feignclient.auth.service.name}", url = "${feignclient.auth.service.url}", path="/internal/decode")
public interface JwtService {
    
    @PostMapping("/username")
    String decodeAndExtractUsername(@RequestBody AccessTokenRequest accessToken);

}
