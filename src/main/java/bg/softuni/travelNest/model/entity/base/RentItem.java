package bg.softuni.travelNest.model.entity.base;

import bg.softuni.travelNest.model.entity.CityEntity;
import bg.softuni.travelNest.model.entity.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@MappedSuperclass
public abstract class RentItem extends BaseEntityUuid{

    @ManyToOne
    @JoinColumn(name = "city_id", referencedColumnName = "id")
    private CityEntity city;

    @Column(nullable = false)
    private String address;

    @Column(nullable = false)
    private BigDecimal price;

    @Column(name = "picture_url", nullable = false)
    private String pictureUrl;

    @ManyToOne
    private User owner;

    public RentItem(CityEntity city, String address, BigDecimal price, String pictureUrl, User owner) {
        this.city = city;
        this.address = address;
        this.price = price;
        this.pictureUrl = pictureUrl;
        this.owner = owner;
    }

    public RentItem setCity(CityEntity city) {
        this.city = city;
        return this;
    }

    public RentItem setPictureUrl(String pictureUrl) {
        this.pictureUrl = pictureUrl;
        return this;
    }

    public RentItem setOwner(User owner) {
        this.owner = owner;
        return this;
    }
}
