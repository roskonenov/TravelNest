package bg.softuni.travelNest.model.entity;

import bg.softuni.travelNest.model.entity.base.BaseEntityId;
import bg.softuni.travelNest.model.enums.RoleEnum;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "roles")
public class Role extends BaseEntityId {

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, unique = true)
    private RoleEnum role;

    public Role(RoleEnum role) {
        this.role = role;
    }
}
