package bg.softuni.travelNest.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface PictureService {

    String uploadImage(MultipartFile image) throws IOException;
}
