package tiduswr.RealTimeChat.model.security;

import lombok.*;

@Getter @Setter @NoArgsConstructor
@AllArgsConstructor @Builder
public class AuthResponse {
    private JwtToken refreshToken;
    private JwtToken token;
}
