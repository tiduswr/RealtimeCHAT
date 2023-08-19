package com.tiduswr.rcauth.models;

import lombok.*;

@Getter @Setter @NoArgsConstructor
@AllArgsConstructor @Builder
public class AccessTokenRequest {
    private String accessToken;
}
