package com.tiduswr.rcauth.feignclients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.tiduswr.rcauth.models.User;
import com.tiduswr.rcauth.models.dto.InternalUserDTO;

@Component
@FeignClient(contextId = "user-service", name = "rc-user", path = "/internal/user")
public interface UserService {

    @PostMapping("/create")
    InternalUserDTO createUser(@RequestBody InternalUserDTO dto);

    @GetMapping("/find/by/username/{username}")
    public User findUserByUsername(@PathVariable String username) throws UsernameNotFoundException;

}
