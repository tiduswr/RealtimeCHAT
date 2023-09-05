package com.tiduswr.rcauth.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.tiduswr.rcauth.feignclients.UserService;
import com.tiduswr.rcauth.models.dto.InternalUserDTO;

@Service
public class SecurityUserDetails implements UserDetailsService{

    @Autowired
    private UserService userService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        InternalUserDTO user = userService.findUserByUsername(username);

        return org.springframework.security.core.userdetails.User
                .builder()
                .username(user.getUserName())
                .password(user.getPassword())
                .roles("USER")
                .build();
    }
    
}
