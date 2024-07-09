package bg.softuni.travelNest.model.entity.commentEntity;

import bg.softuni.travelNest.model.entity.HousingRental;
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
@DiscriminatorValue("housing")
public class HousingComment extends Comment {

    private static final String type = "HOUSING";

    @ManyToOne
    @JoinColumn(name = "housing_rental_id", referencedColumnName = "id")
    private HousingRental housingRental;

    public HousingComment() {
        super(type);
    }
}
