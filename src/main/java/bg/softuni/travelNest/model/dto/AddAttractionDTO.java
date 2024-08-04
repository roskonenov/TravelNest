package bg.softuni.travelNest.model.dto;

import bg.softuni.travelNest.validation.annotation.ImageType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
public class AddAttractionDTO {

    @NotBlank(message = "{attraction.title.not.empty}")
    private String title;

    @NotBlank(message = "{property.city.not.empty}")
    private String city;

    @NotBlank(message = "{property.address.not.empty}")
    private String address;

    @NotNull(message = "{property.file.not.empty}")
    @ImageType(message = "{property.file.proper.type}")
    private MultipartFile image;

    @NotNull(message = "{property.price.not.empty}")
    @PositiveOrZero(message = "{property.price.positive}")
    private BigDecimal price;

    @NotBlank(message = "{attraction.description.not.empty}")
    private String description;
}