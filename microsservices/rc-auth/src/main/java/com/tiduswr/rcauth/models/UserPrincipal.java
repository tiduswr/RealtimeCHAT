package com.tiduswr.rcauth.models;

import org.springframework.security.core.userdetails.UserDetails;

import java.security.Principal;

public class UserPrincipal implements Principal {
    private final String userName;

    public UserPrincipal(UserDetails userDetails) {
        this.userName = userDetails.getUsername();
    }

    @Override
    public String getName() {
        return userName;
    }
}
