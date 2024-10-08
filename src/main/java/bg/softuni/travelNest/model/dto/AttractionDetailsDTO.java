package bg.softuni.travelNest.model.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
public class AttractionDetailsDTO {

    private UUID id;

    private String title;

    private String city;

    private String address;

    private String pictureUrl;

    private BigDecimal price;

    private String description;

    private boolean isPaid;

    private LocalDate startDate;

    private LocalDate endDate;

    private LocalTime time;

    public AttractionDetailsDTO setCity(String cityName) {
        this.city = cityName;
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
