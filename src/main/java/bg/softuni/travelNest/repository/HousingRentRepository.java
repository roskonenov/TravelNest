package bg.softuni.travelNest.repository;

import bg.softuni.travelNest.model.entity.Housing;
import bg.softuni.travelNest.model.entity.rentEntity.HousingRentPeriod;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface HousingRentRepository extends JpaRepository<HousingRentPeriod, Long> {

    @Modifying
    @Query(value = "DELETE FROM HousingRentPeriod hr WHERE hr.housing = :housing")
    void deleteAllWhereHousing(Housing housing);
}
