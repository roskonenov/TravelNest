package bg.softuni.travelNest.model.entity;

import bg.softuni.travelNest.model.entity.base.BaseEntity;
import bg.softuni.travelNest.model.entity.commentEntity.HousingComment;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Getter
@Setter
@Entity
@Table(name = "housing_rental")
public class HousingRental extends BaseEntity {

    @Column(nullable = false, columnDefinition = "TEXT")
    private String address;

    @ManyToOne
    @JoinColumn(name = "city_id", referencedColumnName = "id")
    private CityEntity city;

    @NotNull
    @PositiveOrZero
    private BigDecimal price;

    private Integer floor;

    private Integer rooms;

    @Column(name = "picture_url", columnDefinition = "TEXT")
    private String pictureUrl;

    @Column(name = "is_available")
    private boolean isAvailable;

    @Column(name = "rented_from")
    private LocalDate rentedFrom;

    @Column(name = "rented_to")
    private LocalDate rentedTo;

    @OneToMany(mappedBy = "housingRental")
    private List<HousingComment> comments;

    @ManyToOne
    private User landlord;

    @ManyToOne
    private User tenant;

    public HousingRental() {
        setAvailable(true);
        setComments(new ArrayList<>());
    }

    public HousingRental setPictureUrl(String pictureUrl) {
        this.pictureUrl = pictureUrl;
        return this;
    }

    public HousingRental setCity(CityEntity city) {
        this.city = city;
        return this;
    }

    public HousingRental setLandlord(User landlord) {
        this.landlord = landlord;
        return this;
    }

    public HousingRental setTenant(User tenant) {
        this.tenant = tenant;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        HousingRental that = (HousingRental) o;
        return Objects.equals(getAddress(), that.getAddress()) && Objects.equals(getCity(), that.getCity()) && Objects.equals(getPrice(), that.getPrice()) && Objects.equals(getFloor(), that.getFloor()) && Objects.equals(getRooms(), that.getRooms()) && Objects.equals(getPictureUrl(), that.getPictureUrl());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getAddress(), getCity(), getPrice(), getFloor(), getRooms(), getPictureUrl());
    }
}
