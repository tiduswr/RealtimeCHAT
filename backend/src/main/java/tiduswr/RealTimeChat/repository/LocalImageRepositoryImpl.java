package tiduswr.RealTimeChat.repository;

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

    private final String FOLDER = "./images/";

    @Override
    public void store(String filename, BufferedImage image, String format) throws IOException {

        File outputFile = new File(FOLDER + filename + "." + format);

        checkImagesFolder();

        ImageIO.write(image, format, outputFile);

    }

    @Override
    public byte[] retrieveImage(String filename, String format) throws IOException {

        String imagePath = FOLDER + filename + "." + format;
        FileSystemResource imageResource = new FileSystemResource(imagePath);

        if(!imageResource.exists()){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

        return imageResource.getContentAsByteArray();
    }

    private void checkImagesFolder() {
        File folder = new File(FOLDER);
        if (!folder.exists()) {
            folder.mkdirs();
        }
    }

}
