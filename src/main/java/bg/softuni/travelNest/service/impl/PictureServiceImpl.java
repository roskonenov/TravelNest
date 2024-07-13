package bg.softuni.travelNest.service.impl;

import bg.softuni.travelNest.config.ImgurConfig;
import bg.softuni.travelNest.model.dto.imgurDto.ImgurResponseDTO;
import bg.softuni.travelNest.service.PictureService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClient;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Objects;

@Service

public class PictureServiceImpl implements PictureService {

    private final RestClient restClient;
    private final ImgurConfig imgurConfig;

    public PictureServiceImpl(@Qualifier("imageRestClient") RestClient restClient, ImgurConfig imgurConfig) {
        this.restClient = restClient;
        this.imgurConfig = imgurConfig;
    }

    @Override
    public String uploadImage(MultipartFile image) throws IOException {

        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        ByteArrayResource resource = new ByteArrayResource(image.getBytes()) {
            @Override
            public String getFilename() {
                return image.getOriginalFilename();
            }
        };
        body.add("image", resource);

        return Objects.requireNonNull(restClient
                        .post()
                        .uri(imgurConfig.getUrl())
                        .contentType(MediaType.MULTIPART_FORM_DATA)
                        .body(body)
                        .retrieve()
                        .body(ImgurResponseDTO.class))
                .getData().getLink();
    }
}
