package com.tiduswr.rcuser.util;

import java.io.IOException;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.tiduswr.rcuser.exceptions.ImageNotSupportedException;
import com.tiduswr.rcuser.model.UploadedImage;

import lombok.NoArgsConstructor;

@NoArgsConstructor @Component
public class ImageValidator {
    private void validateImage(MultipartFile file) throws ImageNotSupportedException{
        if(file.isEmpty() || 
        (file.getContentType() != null && !file.getContentType().startsWith("image")))
            throw new ImageNotSupportedException("A imagem não é suportada ou o ContentType está vazio.");
    }

    public UploadedImage validateAndConvert(MultipartFile image, String username) throws ImageNotSupportedException{
        validateImage(image);
        try{
            return new UploadedImage(
                image.getBytes(), 
                image.getContentType(), 
                image.isEmpty()
            );
        }catch(IOException ex){
            throw new ImageNotSupportedException("A imagem não é suportada ou o ContentType está vazio.");
        }
    }
}
