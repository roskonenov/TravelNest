package bg.softuni.travelNest.model.dto;

import bg.softuni.travelNest.model.entity.commentEntity.CarComment;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class CarDetailsDTO {

    private String city;
    private String address;
    private BigDecimal price;
    private String pictureUrl;
    private String make;
    private String model;
    private String engine;
    private Integer doors;
    private List<CarComment> comments;

}
