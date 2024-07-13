package bg.softuni.travelNest.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
public class AddRentalPropertyDTO {

    @NotBlank(message = "{property.address.not.empty}")
    private String address;

    @NotBlank(message = "{property.city.not.empty}")
    private String city;

    @NotNull(message = "{property.price.not.empty}")
    @PositiveOrZero(message = "{property.price.positive}")
    private BigDecimal price;

    private Integer floor;

    private Integer rooms;
}
