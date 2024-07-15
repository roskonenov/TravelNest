package bg.softuni.travelNest.model.entity.base;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "rent_periods")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "type")
public abstract class RentPeriod {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(insertable = false, updatable = false)
    private String type;

    @Column(nullable = false)
    private UUID renter;

    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @Column(name = "end_date")
    private LocalDate endDate;

    protected RentPeriod() {
    }

    protected RentPeriod(String type) {
        this.type = type;
    }

    public RentPeriod(String type, UUID renter, LocalDate startDate, LocalDate endDate) {
        this.type = type;
        this.renter = renter;
        this.startDate = startDate;
        this.endDate = endDate;
    }
}
