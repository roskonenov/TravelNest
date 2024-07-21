package bg.softuni.travelNest.model.entity.rentPeriodEntity;

import bg.softuni.travelNest.model.entity.Car;
import bg.softuni.travelNest.model.entity.base.RentPeriod;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
@Entity
@DiscriminatorValue("car")
public class CarRentPeriod extends RentPeriod {

    private static final String type = "CAR";

    @ManyToOne
    @JoinColumn(name = "car_id", referencedColumnName = "id")
    private Car car;

    protected CarRentPeriod() {
        super(type);
    }

    public CarRentPeriod(UUID renter, LocalDate startDate, LocalDate endDate, Car car) {
        super(type, renter, startDate, endDate);
        this.car = car;
    }
}
