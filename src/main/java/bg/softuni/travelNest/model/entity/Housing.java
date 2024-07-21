package bg.softuni.travelNest.model.entity;

import bg.softuni.travelNest.model.entity.base.RentItem;
import bg.softuni.travelNest.model.entity.commentEntity.HousingComment;
import bg.softuni.travelNest.model.entity.rentPeriodEntity.HousingRentPeriod;
import jakarta.persistence.*;
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
public class Housing extends RentItem {

    @Column(nullable = false)
    private Integer floor;

    @Column(nullable = false)
    private Integer rooms;

    @OneToMany(mappedBy = "housing")
    private List<HousingComment> comments;

    @OneToMany(mappedBy = "housing")
    private List<HousingRentPeriod> rentPeriods;

    public Housing() {
        super();
        setRentPeriods(new ArrayList<>());
        setComments(new ArrayList<>());
    }

    public Housing(CityEntity city, String address, BigDecimal price, Integer floor, Integer rooms, String pictureUrl, User owner) {
        super(city, address, price, pictureUrl, owner);
        this.floor = floor;
        this.rooms = rooms;
        setRentPeriods(new ArrayList<>());
        setComments(new ArrayList<>());
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
