package com.tiduswr.rcuser.resources;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tiduswr.rcuser.exceptions.UserNotFoundException;
import com.tiduswr.rcuser.model.User;
import com.tiduswr.rcuser.model.dto.InternalUserDTO;
import com.tiduswr.rcuser.model.dto.UserDTO;
import com.tiduswr.rcuser.model.dto.UserPasswordRequestDTO;
import com.tiduswr.rcuser.services.UserService;

@RestController
@RequestMapping("/internal/user")
public class InternalUserService {
    
    @Autowired
    private UserService userService;

    @PostMapping("/create")
    public InternalUserDTO createUser(@RequestBody InternalUserDTO dto){
        UserDTO userDTO = userService.createUser(UserDTO.from(dto), dto.getRedirectUrl());

        return InternalUserDTO.from(userDTO);
    }

    @GetMapping("/find/by/username/{username}")
    public User findUserByUsername(@PathVariable String username) throws UserNotFoundException{
        return userService.findUserByUsername(username);
    }

    @GetMapping("/find/by/email/{email}")
    public User findUserByEmail(@PathVariable String email){
        return userService.findUserByEmail(email);
    }

    @PostMapping("/update/password/{username}")
    public void updatePassword(@RequestBody UserPasswordRequestDTO request, @PathVariable("username") String username){
        userService.updatePassword(username, request);
    }
}
