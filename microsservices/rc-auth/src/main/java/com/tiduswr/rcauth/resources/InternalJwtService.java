package com.tiduswr.rcauth.resources;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tiduswr.rcauth.models.AccessTokenRequest;
import com.tiduswr.rcauth.services.JwtService;

@RestController
@RequestMapping("/internal/decode")
public class InternalJwtService {
    
    @Autowired
    private JwtService jwtService;

    @PostMapping("/username")
    public String decodeAndExtractUsername(@RequestBody AccessTokenRequest  accessToken){
        return jwtService.extractUsername(accessToken.getAccessToken());
    }

}
