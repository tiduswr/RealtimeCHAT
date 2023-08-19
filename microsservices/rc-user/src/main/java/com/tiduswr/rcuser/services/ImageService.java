package com.tiduswr.rcuser.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.tiduswr.rcuser.exceptions.ImageNotSupportedException;
import com.tiduswr.rcuser.model.ProfileImage;
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

    @Transactional
    public void saveProfileImage(String username, MultipartFile file) throws IOException, ImageNotSupportedException{

        if(file.isEmpty())
            throw new ImageNotSupportedException("A imagem não é suportada ou o ContentType está vazio.");

        var user = userService.findUserByUsername(username);
        ProfileImage profileImage = profileImageRepository
                .findByUserName(username)
                .orElseGet(() ->
                    profileImageRepository.save(ProfileImage.builder().user(user).build())
                );

        BufferedImage image = getImage(file)
                .map(this::convertToPng)
                .orElseThrow(() ->
                        new ImageNotSupportedException("A imagem não é suportada ou o ContentType está vazio.")
                );

        localFileRepository.store(profileImage.getId().toString(), image, "png");

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

    private Optional<BufferedImage> getImage(MultipartFile file) {
        if (file.getContentType() != null && !file.getContentType().startsWith("image"))
            return Optional.empty();

        try {
            BufferedImage image = ImageIO.read(file.getInputStream());
            return image != null ? Optional.of(image) : Optional.empty();
        } catch (Exception ex) {
            return Optional.empty();
        }
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
