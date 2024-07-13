package bg.softuni.travelNest.repository;

import bg.softuni.travelNest.model.entity.HousingRental;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;
@Repository
public interface HousingRepository extends JpaRepository<HousingRental, UUID> {
}
