package bg.softuni.travelNest.model.entity;

import bg.softuni.travelNest.model.entity.base.BaseEntity;
import bg.softuni.travelNest.model.entity.base.Comment;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.*;

@Getter
@Setter
@Entity
@Table(name = "users")
public class User extends BaseEntity {

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @OneToMany(mappedBy = "user")
    private List<Comment> comments;

    @ManyToMany
    @JoinTable(name = "users_housing_rentals",
            joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "housing_rental_id", referencedColumnName = "id"))
    private Set<HousingRental> favorites;

    @OneToMany(mappedBy = "tenant")
    private Set<HousingRental> rentedHousing;

    @OneToMany(mappedBy = "landlord")
    private Set<HousingRental> myHousingAdds;

    public User() {
        setComments(new ArrayList<>());
        setFavorites(new HashSet<>());
        setMyHousingAdds(new HashSet<>());
        setRentedHousing(new HashSet<>());
    }

    public User setUsername(String username) {
        this.username = username;
        return this;
    }

    public User setEmail(String email) {
        this.email = email;
        return this;
    }

    public User setPassword(String password) {
        this.password = password;
        return this;
    }

    @Override
    public String toString() {
        return "User{" +
                "username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", password='" + (password == null ? "N/A" : "PROVIDED") + '\'' +
                '}';
    }
}
