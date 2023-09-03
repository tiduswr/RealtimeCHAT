package tiduswr.RealTimeChat.model.security;

import lombok.*;

@Getter @Setter @NoArgsConstructor
@AllArgsConstructor @Builder
public class RefreshTokenRequest {
    private String refreshToken;
}
