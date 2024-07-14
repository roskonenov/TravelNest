package bg.softuni.travelNest.repository;

import bg.softuni.travelNest.model.entity.CityEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;
@Repository
public interface CityRepository extends JpaRepository<CityEntity, UUID> {

    CityEntity findByName(String city);
}
