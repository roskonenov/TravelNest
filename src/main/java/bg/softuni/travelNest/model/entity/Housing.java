package bg.softuni.travelNest.model.entity;

import bg.softuni.travelNest.model.entity.base.BaseEntityUuid;
import bg.softuni.travelNest.model.entity.commentEntity.HousingComment;
import bg.softuni.travelNest.model.entity.rentEntity.HousingRentPeriod;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Getter
@Setter
@Entity
@Table(name = "housing")
public class Housing extends BaseEntityUuid {

    @ManyToOne
    @JoinColumn(name = "city_id", referencedColumnName = "id")
    private CityEntity city;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String address;

    @NotNull
    @PositiveOrZero
    private BigDecimal price;

    private Integer floor;

    private Integer rooms;

    @Column(name = "picture_url", columnDefinition = "TEXT")
    private String pictureUrl;

    @OneToMany(mappedBy = "housing")
    private List<HousingComment> comments;

    @OneToMany(mappedBy = "housing")
    private List<HousingRentPeriod> rentPeriods;

    @ManyToOne
    private User landlord;

    public Housing() {
        setComments(new ArrayList<>());
        setRentPeriods(new ArrayList<>());
    }

    public Housing(CityEntity city, String address, BigDecimal price, Integer floor, Integer rooms, String pictureUrl, User landlord) {
        this();
        this.city = city;
        this.address = address;
        this.price = price;
        this.floor = floor;
        this.rooms = rooms;
        this.pictureUrl = pictureUrl;
        this.landlord = landlord;
    }

    public Housing setPictureUrl(String pictureUrl) {
        this.pictureUrl = pictureUrl;
        return this;
    }

    public Housing setCity(CityEntity city) {
        this.city = city;
        return this;
    }

    public Housing setLandlord(User landlord) {
        this.landlord = landlord;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Housing that = (Housing) o;
        return Objects.equals(getAddress(), that.getAddress()) && Objects.equals(getCity(), that.getCity()) && Objects.equals(getPrice(), that.getPrice()) && Objects.equals(getFloor(), that.getFloor()) && Objects.equals(getRooms(), that.getRooms()) && Objects.equals(getPictureUrl(), that.getPictureUrl());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getAddress(), getCity(), getPrice(), getFloor(), getRooms(), getPictureUrl());
    }
}
