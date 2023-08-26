package com.tiduswr.rcauth.services;

import java.util.UUID;

import org.apache.commons.lang.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
import com.tiduswr.rcauth.models.EmailTemplateType;
import com.tiduswr.rcauth.models.JwtTokenType;
import com.tiduswr.rcauth.models.PasswordRecover;
import com.tiduswr.rcauth.models.RefreshToken;
import com.tiduswr.rcauth.models.RefreshTokenRequest;
import com.tiduswr.rcauth.models.User;
import com.tiduswr.rcauth.models.dto.EmailDTO;
import com.tiduswr.rcauth.models.dto.InternalUserDTO;
import com.tiduswr.rcauth.models.dto.PasswordRecoverRequest;
import com.tiduswr.rcauth.models.dto.UserPasswordRequestDTO;
import com.tiduswr.rcauth.rabbitmq.EmailPublisher;
import com.tiduswr.rcauth.repositories.PasswordRecoverRepository;

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

    @Value("${email.sender}")
    private String emailAddress;

    @Autowired
    private PasswordRecoverRepository passwordRecoverRepository;

    @Autowired
    private EmailPublisher emailPublisher;

    private AuthResponse generateAuthResponse(String username) throws WeakSecretJWT{
        AuthResponse authResponse = AuthResponse.builder()
                .token(jwtService.generateToken(username))
                .refreshToken(jwtService.generateRefreshToken(username))
                .build();
        refreshTokenService.removeAllRefreshTokenByUser(username);
        refreshTokenService.storeRefreshToken(authResponse.getRefreshToken().getJwtToken(), username);
        return authResponse;
    }

    @Transactional(readOnly = false)
    public void generateRecoverPasswordRequest(PasswordRecoverRequest request){
        try{
            User user = userService.findUserByEmail(request.getEmail());
            String generatedCode = UUID.randomUUID().toString();
            String redirectUrl = request.getRedirectPrefix().concat(generatedCode);

            PasswordRecover passwordRecover = PasswordRecover.builder()
                .code(generatedCode)
                .user(user)
                .build();

            passwordRecoverRepository.save(passwordRecover);

            EmailDTO emailDTO = generateRecoverEmailDTO(redirectUrl, user, request);
            emailPublisher.publishSendEmailRequest(emailDTO);
        }catch(Exception ex){
            // Não informe nada sobre um dado tão importante como o email,
            // apenas avise que se o email existir na base de dados vai chegar nele.
        }
    }

    @Transactional(readOnly = true)
    public void validateRecoverPasswordRequest(String code, UserPasswordRequestDTO request){
        PasswordRecover passwordRecover = passwordRecoverRepository.findByCode(code)
            .orElseThrow(() -> new UnauthorizedException("Código de recuperação inválido!"));
        
        userService.updatePassword(request, passwordRecover.getUser().getUserName());
        passwordRecoverRepository.delete(passwordRecover);
    }

    private EmailDTO generateRecoverEmailDTO(String redirectUrl, User user, PasswordRecoverRequest request){
        return EmailDTO.builder()
            .action_url(redirectUrl)
            .browser_name(request.getBrowserName())
            .emailFrom(emailAddress)
            .emailSubject("Solicitação de recuperação de Senha | RealTimeCHAT")
            .emailTemplateType(EmailTemplateType.RECOVER_PASSWORD)
            .emailTo(user.getEmail())
            .formalName(user.getFormalName())
            .operating_system(request.getOperatingSystem())
            .ownerId(user.getId())
            .build();
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
