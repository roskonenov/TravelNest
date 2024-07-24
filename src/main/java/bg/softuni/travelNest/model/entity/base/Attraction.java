package bg.softuni.travelNest.model.entity.base;

import bg.softuni.travelNest.model.entity.CityEntity;
import jakarta.persistence.Column;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@MappedSuperclass
public abstract class Attraction extends BaseEntityUuid {

    @Column(nullable = false)
    private String title;

    @ManyToOne
    @JoinColumn(name = "city_id", referencedColumnName = "id", nullable = false)
    private CityEntity city;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String address;

    private BigDecimal price;

    @Column(name = "picture_url", columnDefinition = "TEXT", nullable = false)
    private String pictureUrl;

    @Column(nullable = false)
    private boolean isPaid;
}
