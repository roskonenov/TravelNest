package bg.softuni.travelNest.model.dto;

import bg.softuni.travelNest.model.entity.commentEntity.HousingComment;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class HousingDetailsDTO {

    private String type;
    private String city;
    private String address;
    private BigDecimal price;
    private Integer floor;
    private Integer rooms;
    private String pictureUrl;
    private List<HousingComment> comments;
}
