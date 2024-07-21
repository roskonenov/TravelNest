package bg.softuni.travelNest.model.entity;

import bg.softuni.travelNest.model.entity.base.RentItem;
import bg.softuni.travelNest.model.entity.commentEntity.CarComment;
import bg.softuni.travelNest.model.entity.rentPeriodEntity.CarRentPeriod;
import bg.softuni.travelNest.model.enums.Engine;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Getter
@Setter
@Entity
@Table(name = "cars")
public class Car extends RentItem {

    private String make;

    private String model;

    private Engine engine;

    private Integer doors;

    @OneToMany(mappedBy = "car")
    private List<CarComment> comments;

    @OneToMany(mappedBy = "car")
    private List<CarRentPeriod> rentPeriods;

    public Car() {
        super();
        setRentPeriods(new ArrayList<>());
        setComments(new ArrayList<>());
    }

    public Car(CityEntity city, String address, BigDecimal price, String pictureUrl, User owner, String make, String model, Engine engine, Integer doors) {
        super(city, address, price, pictureUrl, owner);
        this.make = make;
        this.model = model;
        this.engine = engine;
        this.doors = doors;
        setRentPeriods(new ArrayList<>());
        setComments(new ArrayList<>());
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (!(object instanceof Car car)) return false;
        return Objects.equals(getOwner(), car.getOwner()) &&Objects.equals(getPrice(), car.getPrice()) &&Objects.equals(getAddress(), car.getAddress()) &&Objects.equals(getCity(), car.getCity()) &&Objects.equals(getMake(), car.getMake()) && Objects.equals(getModel(), car.getModel()) && getEngine() == car.getEngine() && Objects.equals(getDoors(), car.getDoors());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getOwner(), getCity(), getAddress(), getPrice(), getMake(), getModel(), getEngine(), getDoors());
    }
}
