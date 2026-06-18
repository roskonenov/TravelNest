package bg.softuni.travelNest.model.entity.commentEntity;

import bg.softuni.travelNest.model.entity.Housing;
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
@DiscriminatorValue("housing")
public class HousingComment extends Comment {

    private static final String type = "housing";

    @ManyToOne
    @JoinColumn(name = "housing_id", referencedColumnName = "id")
    private Housing housing;

    public HousingComment() {
        super(type);
    }

    public HousingComment(String text, Housing housing, User owner) {
        super(type, text, owner );
        this.housing = housing;
    }
}

