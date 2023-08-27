package com.tiduswr.rcuser.resources;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.tiduswr.rcuser.feignclients.JwtService;
import com.tiduswr.rcuser.feignclients.PasswordService;
import com.tiduswr.rcuser.model.AccessTokenRequest;
import com.tiduswr.rcuser.model.UploadedImage;
import com.tiduswr.rcuser.model.dto.EmailRequestDTO;
import com.tiduswr.rcuser.model.dto.ImageDTO;
import com.tiduswr.rcuser.model.dto.PublicUserDTO;
import com.tiduswr.rcuser.model.dto.UserDTO;
import com.tiduswr.rcuser.model.dto.UserEmailRequestDTO;
import com.tiduswr.rcuser.model.dto.UserFormalNameRequestDTO;
import com.tiduswr.rcuser.model.dto.UserPasswordRequestDTO;
import com.tiduswr.rcuser.rabbitmq.ProfileImagePublisher;
import com.tiduswr.rcuser.services.UserService;
import com.tiduswr.rcuser.util.ImageValidator;
import com.tiduswr.rcuser.exceptions.ImageNotSupportedException;
import com.tiduswr.rcuser.feignclients.ImageService;

import java.net.UnknownHostException;
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

    @Autowired
    private ProfileImagePublisher profileImagePublisher;

    @Autowired
    private ImageValidator imageValidator;

    @PostMapping("/upload_profile_image")
    @ResponseStatus(HttpStatus.OK)
    public void uploadImage(@RequestParam("image") MultipartFile image,
                            @RequestHeader("Authorization") String auth) throws ImageNotSupportedException {
        
        var username = jwtService.decodeAndExtractUsername(new AccessTokenRequest(auth));
        UploadedImage uploadedImage = imageValidator.validateAndConvert(image, username);
        
        profileImagePublisher.publishProfileImageSaveRequest(new ImageDTO(uploadedImage, username));
    }

    @GetMapping("/retrieve_profile_image/{username}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<?> getImage(@PathVariable("username") String username) throws ImageNotSupportedException {

        HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.IMAGE_PNG);

            byte[] imageBytes = imageService.retrieveProfileImageByUsername(username);
            
            return ResponseEntity.ok()
                .headers(headers)
                .body(imageBytes);

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

    @PostMapping("/edit/email")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateEmail(@RequestHeader("Authorization") String auth,
                                    @RequestBody @Valid EmailRequestDTO dto){

        var username = jwtService.decodeAndExtractUsername(new AccessTokenRequest(auth));
        userService.updateEmail(username, dto);
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
