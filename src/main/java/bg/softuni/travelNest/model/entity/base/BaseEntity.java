package bg.softuni.travelNest.model.entity.base;

import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import org.hibernate.annotations.UuidGenerator;

import java.util.UUID;

@MappedSuperclass
public class BaseEntity {

    @Id
    @UuidGenerator
    private UUID id;

    public UUID getId() {
        return id;
    }

    public BaseEntity setId(UUID id) {
        this.id = id;
        return this;
    }
}
