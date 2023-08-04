package tiduswr.RealTimeChat.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tiduswr.RealTimeChat.exceptions.UnauthorizedException;
import tiduswr.RealTimeChat.model.security.RefreshToken;
import tiduswr.RealTimeChat.model.User;
import tiduswr.RealTimeChat.repository.TokenRepository;

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
