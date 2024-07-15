package bg.softuni.travelNest.repository;

import bg.softuni.travelNest.model.entity.rentEntity.HousingRentPeriod;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HousingRentRepository extends JpaRepository<HousingRentPeriod, Long> {
}
