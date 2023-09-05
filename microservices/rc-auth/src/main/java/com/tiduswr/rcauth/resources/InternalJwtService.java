package com.tiduswr.rcauth.resources;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.tiduswr.rcauth.feignclients.UserService;
import com.tiduswr.rcauth.models.AccessTokenRequest;
import com.tiduswr.rcauth.models.JwtTokenType;
import com.tiduswr.rcauth.models.dto.InternalUserDTO;
import com.tiduswr.rcauth.services.JwtService;

@RestController
@RequestMapping("/internal/decode")
public class InternalJwtService {
    
    @Autowired
    private JwtService jwtService;

    @Autowired
    private UserService userService;

    @PostMapping("/username")
    @ResponseStatus(HttpStatus.OK)
    public String decodeAndExtractUsername(@RequestBody AccessTokenRequest  accessToken){
        return jwtService.extractUsername(accessToken.getAccessToken());
    }

    @PostMapping("/validby/{username}")
    @ResponseStatus(HttpStatus.OK)
    boolean validateAccessToken(@RequestBody AccessTokenRequest accessToken, @PathVariable String username){
        InternalUserDTO user = userService.findUserByUsername(username);
        return jwtService.validateToken(accessToken.getAccessToken(), user, JwtTokenType.ACCESS);
    }

}
