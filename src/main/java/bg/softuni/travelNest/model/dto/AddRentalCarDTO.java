package bg.softuni.travelNest.model.dto;

import bg.softuni.travelNest.model.enums.Engine;
import bg.softuni.travelNest.validation.annotation.ImageType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
public class AddRentalCarDTO {

    @NotBlank(message = "{property.address.not.empty}")
    private String address;

    @NotBlank(message = "{property.city.not.empty}")
    private String city;

    @NotNull(message = "{property.price.not.empty}")
    @PositiveOrZero(message = "{property.price.positive}")
    private BigDecimal price;

    @NotBlank(message = "{property.make.not.empty}")
    private String make;

    @NotBlank(message = "{property.model.not.empty}")
    private String model;

    @NotNull(message = "{property.engine.not.empty}")
    private String engine;

    @NotNull(message = "{property.doors.not.empty}")
    @Positive(message = "{property.doors.positive}")
    private Integer doors;

    @NotNull(message = "{property.file.not.empty}")
    @ImageType(message = "{property.file.proper.type}")
    private MultipartFile image;
}
