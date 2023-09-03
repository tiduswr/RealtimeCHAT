package com.tiduswr.rcauth.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tiduswr.rcauth.exceptions.UnauthorizedException;
import com.tiduswr.rcauth.feignclients.UserService;
import com.tiduswr.rcauth.models.RefreshToken;
import com.tiduswr.rcauth.models.User;
import com.tiduswr.rcauth.repositories.TokenRepository;

@Service
@SuppressWarnings("unused")
public class RefreshTokenService {

    @Autowired
    private TokenRepository tokenRepository;

    @Autowired
    private UserService userService;

    @Transactional
    public void storeRefreshToken(String token, String username){
        User user = userService.findUserByUsername(username);
        RefreshToken refreshToken = RefreshToken.builder()
                .id(null).token(token).user(user).build();
        tokenRepository.save(refreshToken);
    }

    @Transactional
    public void removeAllRefreshTokenByUser(String username) {
        User user = userService.findUserByUsername(username);
        tokenRepository.deleteByUser(user);
    }

    @Transactional(readOnly = true)
    public boolean refreshTokenExists(String token){
        return tokenRepository.existsByToken(token);
    }

    @Transactional(readOnly = true)
    public RefreshToken findTokenByToken(String token) throws UnauthorizedException {
        return tokenRepository.findByToken(token)
                .orElseThrow(() -> new UnauthorizedException("Refresh Token Revogado!"));
    }

    @Transactional
    public void removeRefreshToken(String token){
        tokenRepository.findByToken(token)
                .ifPresent(value -> tokenRepository.delete(value));
    }

}
