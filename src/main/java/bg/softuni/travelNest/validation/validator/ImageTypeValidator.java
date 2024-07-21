package bg.softuni.travelNest.validation.validator;

import bg.softuni.travelNest.validation.annotation.ImageType;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.http.MediaType;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public class ImageTypeValidator implements ConstraintValidator<ImageType, MultipartFile> {

    private static final List<String> VALID_IMAGE_TYPES = List.of(
            MediaType.IMAGE_JPEG_VALUE,
            MediaType.IMAGE_GIF_VALUE,
            MediaType.IMAGE_PNG_VALUE
    );

    @Override
    public void initialize(ImageType constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(MultipartFile value, ConstraintValidatorContext context) {
        return VALID_IMAGE_TYPES.contains(value.getContentType());
    }
}
