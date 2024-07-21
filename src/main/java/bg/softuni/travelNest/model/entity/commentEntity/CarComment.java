package bg.softuni.travelNest.model.entity.commentEntity;

import bg.softuni.travelNest.model.entity.Car;
import bg.softuni.travelNest.model.entity.User;
import bg.softuni.travelNest.model.entity.base.Comment;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@DiscriminatorValue("car")
public class CarComment extends Comment {

    private static final String type = "CAR";

    @ManyToOne
    @JoinColumn(name = "car_id", referencedColumnName = "id")
    private Car car;

    protected CarComment() {
        super(type);
    }

    public CarComment(String text, User owner, Car car) {
        super(type, text, owner);
        this.car = car;
    }
}
