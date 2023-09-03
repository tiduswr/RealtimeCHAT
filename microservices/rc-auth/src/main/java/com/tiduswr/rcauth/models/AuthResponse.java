package com.tiduswr.rcauth.models;

import lombok.*;

@Getter @Setter @NoArgsConstructor
@AllArgsConstructor @Builder @ToString
public class AuthResponse {
    private JwtToken refreshToken;
    private JwtToken token;
}
