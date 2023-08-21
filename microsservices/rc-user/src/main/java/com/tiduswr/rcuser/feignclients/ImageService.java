package com.tiduswr.rcuser.feignclients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.tiduswr.rcuser.exceptions.ImageNotSupportedException;

@Component
@FeignClient(name = "rc-image-worker", path = "/internal/images")
public interface ImageService {
    
    @GetMapping("/users/profile_image/{username}")
    byte[] retrieveProfileImageByUsername(@PathVariable("username") String username) throws ImageNotSupportedException;

}
