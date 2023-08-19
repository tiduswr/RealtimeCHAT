package com.tiduswr.rcauth.models;

import lombok.*;

@Getter @Setter @NoArgsConstructor
@AllArgsConstructor @Builder
public class RefreshTokenRequest {
    private String refreshToken;
}
