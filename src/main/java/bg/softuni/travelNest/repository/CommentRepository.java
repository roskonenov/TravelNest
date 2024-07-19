package bg.softuni.travelNest.repository;

import bg.softuni.travelNest.model.entity.Housing;
import bg.softuni.travelNest.model.entity.base.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;
@Repository
public interface CommentRepository extends JpaRepository<Comment, UUID> {

    List<Comment> findByType(String type);

    @Modifying
    @Query(value = "DELETE FROM HousingComment hc WHERE hc.housing = :housing")
    void deleteAllWhereHousing(Housing housing);
}
