package com.tiduswr.rcmessage.model;

import lombok.*;

@Getter @Setter @NoArgsConstructor
@AllArgsConstructor @Builder
public class AccessTokenRequest {
    private String accessToken;
}
