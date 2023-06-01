package tiduswr.RealTimeChat.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.*;
import tiduswr.RealTimeChat.model.security.AuthRequest;
import tiduswr.RealTimeChat.model.security.AuthResponse;
import tiduswr.RealTimeChat.model.security.RefreshTokenRequest;
import tiduswr.RealTimeChat.model.User;
import tiduswr.RealTimeChat.services.AuthService;
import tiduswr.RealTimeChat.services.UserService;

@RestController
public class AuthRestController {

    @Autowired
    private UserService userService;
    @Autowired
    private AuthService authService;
    @Autowired
    private AuthenticationManager authenticationManager;

    @PostMapping("/signin")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<User> createUser(@RequestBody User user) {
        User createdUser = userService.createUser(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
    }

    @PostMapping("/auth")
    @ResponseStatus(HttpStatus.OK)
    public AuthResponse authUser(@RequestBody AuthRequest authRequest){
        return authService.authUser(authRequest);
    }

    @PostMapping("/refreshtoken")
    @ResponseStatus(HttpStatus.OK)
    public AuthResponse refreshToken(@RequestBody RefreshTokenRequest request){
        return authService.refreshToken(request);
    }

    @PostMapping("/logout")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void logout(@RequestBody RefreshTokenRequest request){
        authService.killRefreshToken(request);
    }

}
