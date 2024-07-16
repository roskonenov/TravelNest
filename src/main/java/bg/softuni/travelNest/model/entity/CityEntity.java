package bg.softuni.travelNest.model.entity;

import bg.softuni.travelNest.model.entity.base.BaseEntityUuid;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "city")
public class CityEntity extends BaseEntityUuid {

    @Column(nullable = false)
    private String name;

    @OneToMany(mappedBy = "city")
    private List<Housing> housing;

    public CityEntity(String name) {
        this.name = name;
    }
}
