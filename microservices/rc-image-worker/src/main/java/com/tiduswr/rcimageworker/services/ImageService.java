package com.tiduswr.rcimageworker.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.tiduswr.rcimageworker.exceptions.ImageNotSupportedException;
import com.tiduswr.rcimageworker.feignclients.UserService;
import com.tiduswr.rcimageworker.models.ProfileImage;
import com.tiduswr.rcimageworker.models.UploadedImage;
import com.tiduswr.rcimageworker.repositories.LocalImageRepository;
import com.tiduswr.rcimageworker.repositories.ProfileImageRepository;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
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

    @Transactional
    public void saveProfileImage(String username, UploadedImage file) throws IOException, ImageNotSupportedException{
        
        var user = userService.findUserByUsername(username);
        ProfileImage profileImage = profileImageRepository
                .findByUserName(username)
                .orElseGet(() ->
                    profileImageRepository.save(ProfileImage.builder().user(user).build())
                );

        BufferedImage image = convertToPng(getImage(file));

        localFileRepository.store(profileImage.getId().toString(), image, "png");

    }

    @Transactional(readOnly = true)
    public byte[] retrieveProfileImageByUsername(String username) throws ImageNotSupportedException {
        Optional<ProfileImage> profileImage = profileImageRepository.findByUserName(username);

        if(profileImage.isPresent()){
            ProfileImage image = profileImage.get();
            
            try{
                return localFileRepository.retrieveImage(image.getId().toString(), "png");
            }catch(IOException ex){
                throw new ImageNotSupportedException("A imagem não é suportada ou o ContentType está vazio.");
            }
        }
        return new byte[]{};
    }

    private BufferedImage getImage(UploadedImage file) throws IOException {
        return ImageIO.read(new ByteArrayInputStream(file.getImageBytes()));
    }

    private BufferedImage convertToPng(BufferedImage image) {

        Image scaledImage = image.getScaledInstance(PROFILE_IMAGE_SIZE, PROFILE_IMAGE_SIZE, Image.SCALE_SMOOTH);
        BufferedImage convertedImage = new BufferedImage(PROFILE_IMAGE_SIZE, PROFILE_IMAGE_SIZE, BufferedImage.TYPE_INT_ARGB);

        Graphics2D g2d = convertedImage.createGraphics();
        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2d.drawImage(scaledImage, 0, 0, null);
        g2d.dispose();

        return convertedImage;

    }

}
