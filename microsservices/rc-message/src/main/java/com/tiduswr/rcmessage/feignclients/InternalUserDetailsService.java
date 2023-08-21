package com.tiduswr.rcmessage.feignclients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseStatus;

@FeignClient(contextId = "userdetails-service", name = "rc-auth", path = "/internal/auth/userdetails")
public interface InternalUserDetailsService {
    
    @GetMapping("/{username}")
    @ResponseStatus(HttpStatus.OK)
    public UserDetails createUserDetails(@PathVariable String username);
}
