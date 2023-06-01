package tiduswr.RealTimeChat.model.security;

import lombok.*;

@Getter @Setter @NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuthRequest {
    private String userName;
    private String password;
}
