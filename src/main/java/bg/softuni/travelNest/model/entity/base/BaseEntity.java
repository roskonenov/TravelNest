package bg.softuni.travelNest.model.entity.base;

import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.UuidGenerator;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@MappedSuperclass
public class BaseEntity {

    @Id
    @UuidGenerator
    private UUID id;
}
