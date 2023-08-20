package com.tiduswr.rcuser.repositories;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Repository;
import org.springframework.web.server.ResponseStatusException;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

@Repository
@SuppressWarnings("unused")
public class LocalImageRepositoryImpl implements LocalImageRepository {

    @Value("${image-repository.url}")
    private String FOLDER;

    @Override
    public byte[] retrieveImage(String filename, String format) throws IOException {

        String imagePath = FOLDER + filename + "." + format;
        FileSystemResource imageResource = new FileSystemResource(imagePath);

        if(!imageResource.exists()){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

        return imageResource.getContentAsByteArray();
    }

}
