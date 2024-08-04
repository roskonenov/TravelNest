package bg.softuni.travelNest.model.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
public class AttractionDTO {

    private UUID id;

    private String title;

    private String cityName;

    private String address;

    private String pictureUrl;

    private BigDecimal price;

    private String description;

    private boolean isPaid;
}
