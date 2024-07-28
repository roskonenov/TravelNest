package bg.softuni.travelNest.repository;

import bg.softuni.travelNest.model.entity.Housing;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
@Repository
public interface HousingRepository extends JpaRepository<Housing, UUID> {

    @Modifying
    @Query(nativeQuery = true, value = "DELETE FROM users_favorite_housings WHERE housing_id = :housingId")
    void deleteAllFromUsersFavoriteHousingsWhereHousingId(UUID housingId);

    @Query(nativeQuery = true, value = "SELECT * FROM housing h JOIN users_favorite_housings ufh ON h.id = ufh.housing_id WHERE ufh.user_id = :userId")
    Optional<List<Housing>> findAllByUserFavorites(UUID userId);
}