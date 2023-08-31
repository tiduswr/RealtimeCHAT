package com.tiduswr.rcuser.feignclients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.tiduswr.rcuser.model.dto.UserPasswordRequestDTO;

import jakarta.validation.Valid;

@Component
@FeignClient(contextId = "password-service", name = "${feignclient.auth.service.name}", 
                url = "${feignclient.auth.service.url}", path = "/internal/password")
public interface PasswordService {
    
    @PostMapping("/check")
    public ResponseEntity<Void> checkPassword(@Valid @RequestBody UserPasswordRequestDTO passwordRequestDTO);

}
