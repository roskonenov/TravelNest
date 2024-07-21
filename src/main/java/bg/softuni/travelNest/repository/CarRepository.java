package bg.softuni.travelNest.repository;

import bg.softuni.travelNest.model.entity.Car;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.UUID;
@Repository
public interface CarRepository extends JpaRepository<Car, UUID> {

    @Modifying
    @Query(nativeQuery = true, value = "DELETE FROM users_favorite_cars WHERE car_id = :carId")
    void deleteAllFromUsersFavoriteCarsWhereHousingId(UUID carId);
}
