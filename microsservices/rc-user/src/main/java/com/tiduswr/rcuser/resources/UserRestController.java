package com.tiduswr.rcuser.resources;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.tiduswr.rcuser.feignclients.JwtService;
import com.tiduswr.rcuser.feignclients.PasswordService;
import com.tiduswr.rcuser.model.AccessTokenRequest;
import com.tiduswr.rcuser.model.dto.PublicUserDTO;
import com.tiduswr.rcuser.model.dto.UserDTO;
import com.tiduswr.rcuser.model.dto.UserFormalNameRequestDTO;
import com.tiduswr.rcuser.model.dto.UserPasswordRequestDTO;
import com.tiduswr.rcuser.services.ImageService;
import com.tiduswr.rcuser.services.UserService;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/users")
@SuppressWarnings("unused")
public class UserRestController {

    @Autowired
    private UserService userService;

    @Autowired
    private ImageService imageService;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private PasswordService passwordService;

    @PostMapping("/upload_profile_image")
    @ResponseStatus(HttpStatus.OK)
    public void uploadImage(@RequestParam("image") MultipartFile image,
                            @RequestHeader("Authorization") String auth) throws IOException {
                                
        var username = jwtService.decodeAndExtractUsername(new AccessTokenRequest(auth));
        imageService.saveProfileImage(username, image);
    }

    @GetMapping("/retrieve_profile_image/{username}")
    @ResponseStatus(HttpStatus.OK)
    public byte[] getImage(@PathVariable("username") String username,
                           HttpServletResponse response) throws IOException {

        response.setContentType(MediaType.IMAGE_PNG_VALUE);
        return imageService.retrieveProfileImageByUsername(username);
    }

    @GetMapping("/retrieve_profile_info/{username}")
    @ResponseStatus(HttpStatus.OK)
    public PublicUserDTO retrieveUserInformation(@PathVariable("username") String username){

        return userService.findPublicUserDtoByUsername(username);
    }

    @GetMapping("/retrieve_profile_info")
    @ResponseStatus(HttpStatus.OK)
    public UserDTO retrieveOwnInformation(@RequestHeader("Authorization") String auth){

        var username = jwtService.decodeAndExtractUsername(new AccessTokenRequest(auth));
        return userService.findUserDtoByUsername(username);
    }

    @PostMapping("/edit/formalname")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateFormalName(@RequestHeader("Authorization") String auth,
                                    @RequestBody @Valid UserFormalNameRequestDTO dto){

        var username = jwtService.decodeAndExtractUsername(new AccessTokenRequest(auth));
        userService.updateFormalName(username, dto);
    }

    @PostMapping("/edit/password")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updatePassword(@RequestHeader("Authorization") String auth,
                                  @RequestBody UserPasswordRequestDTO dto){
        
        passwordService.checkPassword(dto);
        var username = jwtService.decodeAndExtractUsername(new AccessTokenRequest(auth));
        userService.updatePassword(username, dto);
    }

    @GetMapping("/find/{query}")
    @ResponseStatus(HttpStatus.OK)
    public List<PublicUserDTO> findUserByName(@RequestHeader("Authorization") String auth,
                                              @PathVariable("query") String query){

        var username = jwtService.decodeAndExtractUsername(new AccessTokenRequest(auth));
        return userService.getUsersByUsernameOrFormalname(query, username);
    }

}
