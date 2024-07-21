package bg.softuni.travelNest.repository;

import bg.softuni.travelNest.model.entity.Car;
import bg.softuni.travelNest.model.entity.Housing;
import bg.softuni.travelNest.model.entity.base.RentPeriod;
import bg.softuni.travelNest.model.entity.rentPeriodEntity.HousingRentPeriod;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface RentRepository extends JpaRepository<RentPeriod, Long> {

    @Modifying
    @Query(value = "DELETE FROM HousingRentPeriod hr WHERE hr.housing = :housing")
    void deleteAllWhereHousing(Housing housing);

    @Modifying
    @Query(value = "DELETE FROM CarRentPeriod cr WHERE cr.car = :car")
    void deleteAllWhereCar(Car car);

    Optional<List<RentPeriod>> findByEndDateIsLessThanEqual(LocalDate date);
}
