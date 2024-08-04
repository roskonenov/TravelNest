package bg.softuni.travelNest.model.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
public class AttractionDetailsDTO {

    private UUID id;

    private String title;

    private String cityName;

    private String address;

    private String pictureUrl;

    private BigDecimal price;

    private String description;

    private boolean isPaid;

    public AttractionDetailsDTO setCityName(String cityName) {
        this.cityName = cityName;
        return this;
    }

    public AttractionDetailsDTO setPictureUrl(String pictureUrl) {
        this.pictureUrl = pictureUrl;
        return this;
    }

    public AttractionDetailsDTO setPaid(boolean paid) {
        isPaid = paid;
        return this;
    }
}
