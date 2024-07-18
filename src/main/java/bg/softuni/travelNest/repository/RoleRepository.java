package bg.softuni.travelNest.repository;

import bg.softuni.travelNest.model.entity.Role;
import bg.softuni.travelNest.model.enums.RoleEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    Role findByRole(RoleEnum roleEnum);
}
