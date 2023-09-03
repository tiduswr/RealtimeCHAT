package com.tiduswr.rcimageworker.resources;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tiduswr.rcimageworker.exceptions.ImageNotSupportedException;
import com.tiduswr.rcimageworker.services.ImageService;

@RestController
@RequestMapping("/internal/images")
public class InternalImageService {
    
    @Autowired
    private ImageService imageService;

    @GetMapping("/users/profile_image/{username}")
    byte[] retrieveProfileImageByUsername(@PathVariable("username") String username) throws ImageNotSupportedException{
        return imageService.retrieveProfileImageByUsername(username);
    }

}
