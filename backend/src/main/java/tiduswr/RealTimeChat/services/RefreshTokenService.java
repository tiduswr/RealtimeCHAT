package tiduswr.RealTimeChat.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import tiduswr.RealTimeChat.model.security.RefreshToken;
import tiduswr.RealTimeChat.model.User;
import tiduswr.RealTimeChat.repository.TokenRepository;

@Service
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
    public RefreshToken findTokenByToken(String token) throws ResponseStatusException{
        return tokenRepository.findByToken(token)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST));
    }

    @Transactional
    public void removeRefreshToken(String token){
        tokenRepository.findByToken(token)
                .ifPresent(value -> tokenRepository.delete(value));
    }

}
