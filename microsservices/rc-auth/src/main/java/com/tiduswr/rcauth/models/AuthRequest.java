package com.tiduswr.rcauth.models;

import lombok.*;

@Getter @Setter @NoArgsConstructor
@AllArgsConstructor
@Builder @ToString
public class AuthRequest {
    private String userName;
    private String password;
}
