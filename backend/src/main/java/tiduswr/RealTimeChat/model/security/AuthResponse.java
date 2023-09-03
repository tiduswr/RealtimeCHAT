package tiduswr.RealTimeChat.model.security;

import lombok.*;

@Getter @Setter @NoArgsConstructor
@AllArgsConstructor @Builder @ToString
public class AuthResponse {
    private JwtToken refreshToken;
    private JwtToken token;
}
