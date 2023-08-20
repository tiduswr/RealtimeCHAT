package com.tiduswr.rcuser.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.tiduswr.rcuser.exceptions.ImageNotSupportedException;
import com.tiduswr.rcuser.model.AccessTokenRequest;
import com.tiduswr.rcuser.model.ProfileImage;
import com.tiduswr.rcuser.model.UploadedImage;
import com.tiduswr.rcuser.model.dto.ImageDTO;
import com.tiduswr.rcuser.repositories.LocalImageRepository;
import com.tiduswr.rcuser.repositories.ProfileImageRepository;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Optional;

@Service
@SuppressWarnings("unused")
public class ImageService {

    @Autowired
    private ProfileImageRepository profileImageRepository;

    @Autowired
    private LocalImageRepository localFileRepository;

    @Autowired
    private UserService userService;

    private final int PROFILE_IMAGE_SIZE = 100;

    private void validateImage(MultipartFile file) throws ImageNotSupportedException{
        if(file.isEmpty() || 
        (file.getContentType() != null && !file.getContentType().startsWith("image")))
            throw new ImageNotSupportedException("A imagem não é suportada ou o ContentType está vazio.");
    }

    public UploadedImage validateAndConvert(MultipartFile image, String username) throws ImageNotSupportedException, IOException{
        validateImage(image);
        return new UploadedImage(
            image.getBytes(), 
            image.getContentType(), 
            image.isEmpty()
        );
    }

    @Transactional(readOnly = true)
    public byte[] retrieveProfileImageByUsername(String username) throws IOException {
        Optional<ProfileImage> profileImage = profileImageRepository.findByUserName(username);

        if(profileImage.isPresent()){
            ProfileImage image = profileImage.get();
            return localFileRepository.retrieveImage(image.getId().toString(), "png");
        }else{
            return null;
        }
    }

}
