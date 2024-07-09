package bg.softuni.travelNest.model.entity.base;

import bg.softuni.travelNest.model.entity.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "comments")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "type")
public abstract class Comment extends BaseEntity {

    @Column(insertable = false, updatable = false)
    private String type;

    @Column(nullable = false)
    private String text;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false)
    private User user;

    protected Comment(){}

    protected Comment(String type){
        this.type = type;
    }
}
