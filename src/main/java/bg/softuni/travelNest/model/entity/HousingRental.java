package bg.softuni.travelNest.model.entity;

import bg.softuni.travelNest.model.entity.base.BaseEntity;
import bg.softuni.travelNest.model.entity.commentEntity.HousingComment;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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

    @Column(name = "picture_url", columnDefinition = "TEXT")
    private String pictureUrl;

    @Column(name = "is_available", columnDefinition = "BOOL default true")
//    @ColumnDefault("true")
    private boolean isAvailable;

    @Column(name = "rented_from")
    private LocalDate rentedFrom;

    @Column(name = "rented_to")
    private LocalDate rentedTo;

    @OneToMany(mappedBy = "housingRental")
    private List<HousingComment> comments;
}
