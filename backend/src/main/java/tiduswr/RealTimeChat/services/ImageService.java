package tiduswr.RealTimeChat.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import tiduswr.RealTimeChat.exceptions.ImageNotSupportedException;
import tiduswr.RealTimeChat.model.ProfileImage;
import tiduswr.RealTimeChat.repository.LocalImageRepository;
import tiduswr.RealTimeChat.repository.ProfileImageRepository;

import javax.imageio.ImageIO;
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
        ProfileImage profileImage = profileImageRepository.findByUserName(username).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND)
        );
        return localFileRepository.retrieveImage(profileImage.getId().toString(), "png");
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
        BufferedImage convertedImage = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_ARGB);
        convertedImage.createGraphics().drawImage(image, 0, 0, null);
        return convertedImage;
    }

}
