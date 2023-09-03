package com.tiduswr.rcauth.resources;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import com.tiduswr.rcauth.feignclients.UserService;
import com.tiduswr.rcauth.models.AuthRequest;
import com.tiduswr.rcauth.models.AuthResponse;
import com.tiduswr.rcauth.models.RefreshTokenRequest;
import com.tiduswr.rcauth.models.dto.InternalUserDTO;
import com.tiduswr.rcauth.models.dto.PasswordRecoverRequest;
import com.tiduswr.rcauth.models.dto.UserDTO;
import com.tiduswr.rcauth.models.dto.UserPasswordRequestDTO;
import com.tiduswr.rcauth.services.AuthService;

@RestController
public class AuthRestController {

    @Autowired
    private UserService userService;

    @Autowired
    private AuthService authService;

    @PostMapping("/signup")
    @ResponseStatus(HttpStatus.CREATED)
    public UserDTO createUser(@Valid @RequestBody UserDTO user) {
        var userDTO = userService.createUser(InternalUserDTO.from(user));
        return UserDTO.from(userDTO);
    }

    @PostMapping("/auth")
    @ResponseStatus(HttpStatus.OK)
    public AuthResponse authUser(@RequestBody AuthRequest authRequest){
        return authService.authUser(authRequest);
    }

    @PostMapping("/recover_password")
    @ResponseStatus(HttpStatus.OK)
    public void recoverPassword(@Valid @RequestBody PasswordRecoverRequest request){
        authService.generateRecoverPasswordRequest(request);
    }

    @PostMapping("/recover_password/validate/{code}")
    @ResponseStatus(HttpStatus.OK)
    public void validateRecoverPasswordRequest(@Valid @RequestBody UserPasswordRequestDTO request, 
        @PathVariable("code") String code){
        authService.validateRecoverPasswordRequest(code, request);
    }
    
    @PostMapping("/refresh_token")
    @ResponseStatus(HttpStatus.OK)
    public AuthResponse refreshToken(@RequestBody RefreshTokenRequest request){
        return authService.refreshToken(request);
    }

    @PostMapping("/quit")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void logout(@RequestBody RefreshTokenRequest request){
        authService.killRefreshToken(request);
    }

}
