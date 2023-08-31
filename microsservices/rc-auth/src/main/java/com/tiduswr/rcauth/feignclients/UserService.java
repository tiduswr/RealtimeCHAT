package com.tiduswr.rcauth.feignclients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.tiduswr.rcauth.exceptions.UserNotFoundException;
import com.tiduswr.rcauth.models.User;
import com.tiduswr.rcauth.models.dto.InternalUserDTO;
import com.tiduswr.rcauth.models.dto.UserPasswordRequestDTO;

@Component
@FeignClient(contextId = "user-service", name = "${feignclient.user.service.name}", 
                url="${feignclient.user.service.url}", path = "/internal/user")
public interface UserService {

    @PostMapping("/create")
    InternalUserDTO createUser(@RequestBody InternalUserDTO dto);

    @GetMapping("/find/by/username/{username}")
    User findUserByUsername(@PathVariable String username) throws UserNotFoundException;
    
    @GetMapping("/find/by/email/{email}")
    User findUserByEmail(@PathVariable String email) throws UserNotFoundException;

    @PostMapping("/update/password/{username}")
    void updatePassword(@RequestBody UserPasswordRequestDTO request, @PathVariable("username") String username);

}
