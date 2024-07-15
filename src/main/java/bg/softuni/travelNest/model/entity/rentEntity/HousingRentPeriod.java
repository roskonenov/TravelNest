package bg.softuni.travelNest.model.entity.rentEntity;

import bg.softuni.travelNest.model.entity.Housing;
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
@DiscriminatorValue("housing")
public class HousingRentPeriod extends RentPeriod {

    private static final String type = "HOUSING";

    @ManyToOne
    @JoinColumn(name = "housing_id", referencedColumnName = "id")
    private Housing housing;

    protected HousingRentPeriod() {
        super(type);
    }

    public HousingRentPeriod( UUID renter, LocalDate startDate, LocalDate endDate, Housing housing) {
        super(type, renter, startDate, endDate);
        this.housing = housing;
    }
}
