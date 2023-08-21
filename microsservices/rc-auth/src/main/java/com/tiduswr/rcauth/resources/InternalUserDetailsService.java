package com.tiduswr.rcauth.resources;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/internal/auth/userdetails")
public class InternalUserDetailsService {
    
    @Autowired
    private UserDetailsService userDetailsService;

    @GetMapping("/{username}")
    @ResponseStatus(HttpStatus.OK)
    public UserDetails createUserDetails(@PathVariable String username){
        return userDetailsService.loadUserByUsername(username);
    }

}
