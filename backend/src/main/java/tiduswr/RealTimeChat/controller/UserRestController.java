package tiduswr.RealTimeChat.controller;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import tiduswr.RealTimeChat.services.ImageService;
import tiduswr.RealTimeChat.services.JwtService;
import tiduswr.RealTimeChat.services.UserService;

import java.io.IOException;

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

    @PostMapping("/upload_profile_image")
    @ResponseStatus(HttpStatus.OK)
    public void uploadImage(@RequestParam("image") MultipartFile image,
                            @RequestHeader("Authorization") String auth) throws IOException {
        var username = jwtService.extractUsername(auth);
        imageService.saveProfileImage(username, image);
    }

    @GetMapping("/retrieve_profile_image/{username}")
    @ResponseStatus(HttpStatus.OK)
    public byte[] getImage(@PathVariable("username") String username,
                           HttpServletResponse response) throws IOException {
        response.setContentType(MediaType.IMAGE_PNG_VALUE);
        return imageService.retrieveProfileImageByUsername(username);
    }

}
