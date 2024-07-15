package bg.softuni.travelNest.repository;

import bg.softuni.travelNest.model.entity.Housing;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;
@Repository
public interface HousingRepository extends JpaRepository<Housing, UUID> {
}
