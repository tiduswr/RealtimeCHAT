package com.tiduswr.rcauth.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter @Setter @NoArgsConstructor
@AllArgsConstructor
public class JwtToken {

    private String userName;
    private String jwtToken;
    private Date expiration;

}
