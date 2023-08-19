package com.tiduswr.rcauth.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.tiduswr.rcauth.exceptions.UnauthorizedException;
import com.tiduswr.rcauth.exceptions.WeakSecretJWT;
import com.tiduswr.rcauth.feignclients.UserService;
import com.tiduswr.rcauth.models.AuthRequest;
import com.tiduswr.rcauth.models.AuthResponse;
import com.tiduswr.rcauth.models.JwtTokenType;
import com.tiduswr.rcauth.models.RefreshToken;
import com.tiduswr.rcauth.models.RefreshTokenRequest;
import com.tiduswr.rcauth.models.User;

@Service
@SuppressWarnings("unused")
public class AuthService {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private UserService userService;

    @Autowired
    private RefreshTokenService refreshTokenService;

    private AuthResponse generateAuthResponse(String username) throws WeakSecretJWT{
        AuthResponse authResponse = AuthResponse.builder()
                .token(jwtService.generateToken(username))
                .refreshToken(jwtService.generateRefreshToken(username))
                .build();
        refreshTokenService.removeAllRefreshTokenByUser(username);
        refreshTokenService.storeRefreshToken(authResponse.getRefreshToken().getJwtToken(), username);
        return authResponse;
    }

    @Transactional
    public AuthResponse authUser(AuthRequest authRequest){
        Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        authRequest.getUserName(),
                        authRequest.getPassword())
        );
        if(!auth.isAuthenticated()) throw new BadCredentialsException("Credenciais inválidas!");

        return generateAuthResponse(authRequest.getUserName());
    }

    @Transactional
    public AuthResponse refreshToken(RefreshTokenRequest request) throws UnauthorizedException, UsernameNotFoundException {

        RefreshToken refreshToken = refreshTokenService.findTokenByToken(request.getRefreshToken());
        User user = userService.findUserByUsername(refreshToken.getUser().getUserName());

        if(jwtService.validateToken(refreshToken.getToken(), user, JwtTokenType.REFRESH)){
            return generateAuthResponse(refreshToken.getUser().getUserName());
        }else{
            throw new UnauthorizedException("Autenticação falhou, Credenciais inválidas!");
        }
    }

    @Transactional
    public void killRefreshToken(RefreshTokenRequest request) {
        refreshTokenService.removeRefreshToken(request.getRefreshToken());
    }
}
