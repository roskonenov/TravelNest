package bg.softuni.travelNest.model.dto;

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
public class AddRentalHousingDTO {

    @NotBlank(message = "{property.type.not.empty}")
    private String type;

    @NotBlank(message = "{property.address.not.empty}")
    private String address;

    @NotBlank(message = "{property.city.not.empty}")
    private String city;

    @NotNull(message = "{property.price.not.empty}")
    @PositiveOrZero(message = "{property.price.positive}")
    private BigDecimal price;

    @NotNull(message = "{property.floor.not.empty}")
    @Positive(message = "{property.floor.positive}")
    private Integer floor;

    @NotNull(message = "{property.rooms.not.empty}")
    @Positive(message = "{property.rooms.positive}")
    private Integer rooms;

    @NotNull(message = "{property.file.not.empty}")
    @ImageType(message = "{property.file.proper.type}")
    private MultipartFile image;
}
