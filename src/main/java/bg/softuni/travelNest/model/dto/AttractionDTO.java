package bg.softuni.travelNest.model.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
public class AttractionDTO {

    private String title;

    private String cityName;

    private String address;

    private String pictureUrl;

    private BigDecimal prise;

    private String description;

    private boolean isPaid;
}
