package bg.softuni.travelNest.model.entity;

import bg.softuni.travelNest.model.entity.base.BaseEntity;
import bg.softuni.travelNest.model.entity.commentEntity.HousingComment;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
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
    @ColumnDefault("true")
    private boolean isAvailable;

    @Column(name = "rented_from")
    private LocalDate rentedFrom;

    @Column(name = "rented_to")
    private LocalDate rentedTo;

    @OneToMany(mappedBy = "housingRental")
    private List<HousingComment> comments;

    public HousingRental setPictureUrl(String pictureUrl) {
        this.pictureUrl = pictureUrl;
        return this;
    }

    public HousingRental setCity(CityEntity city) {
        this.city = city;
        return this;
    }
}
