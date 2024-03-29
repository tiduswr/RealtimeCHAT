package tiduswr.RealTimeChat.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import tiduswr.RealTimeChat.model.ErrorResponse;
import tiduswr.RealTimeChat.model.dto.PasswordRecoverRequest;
import tiduswr.RealTimeChat.model.dto.UserDTO;
import tiduswr.RealTimeChat.model.dto.UserPasswordRequestDTO;
import tiduswr.RealTimeChat.model.security.AuthRequest;
import tiduswr.RealTimeChat.model.security.AuthResponse;
import tiduswr.RealTimeChat.model.security.RefreshTokenRequest;
import tiduswr.RealTimeChat.services.AuthService;
import tiduswr.RealTimeChat.services.UserService;

@RestController
@SuppressWarnings("unused")
public class AuthRestController {

    @Autowired
    private UserService userService;
    @Autowired
    private AuthService authService;

    @PostMapping("/signup")
    @ResponseStatus(HttpStatus.CREATED)
    public UserDTO createUser(@Valid @RequestBody UserDTO user) {
        return userService.createUser(user);
    }

    @PostMapping("/auth")
    @ResponseStatus(HttpStatus.OK)
    public AuthResponse authUser(@RequestBody AuthRequest authRequest){
        return authService.authUser(authRequest);
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

    @PostMapping("/recover_password")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<ErrorResponse> recoverPassword(@RequestBody PasswordRecoverRequest request){
        ErrorResponse errorResponse = new ErrorResponse(null, "Funcionalidade não suportada!");
        return ResponseEntity.status(HttpStatus.GONE).body(errorResponse);
    }

    @PostMapping("/recover_password/validate/{code}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<ErrorResponse> validateRecoverPasswordRequest(@Valid @RequestBody UserPasswordRequestDTO request, 
        @PathVariable("code") String code){
        ErrorResponse errorResponse = new ErrorResponse(null, "Funcionalidade não suportada!");
        return ResponseEntity.status(HttpStatus.GONE).body(errorResponse);
    }

}
