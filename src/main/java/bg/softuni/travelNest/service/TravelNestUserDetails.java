package bg.softuni.travelNest.service;

import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;
import java.util.Map;
import java.util.UUID;

@Getter
public class TravelNestUserDetails extends User {

    private final UUID uuid;

    private final String email;

    public TravelNestUserDetails(UUID uuid,
                                 String username,
                                 String password,
                                 Collection<? extends GrantedAuthority> authorities,
                                 String email) {
        super(username, password, authorities);
        this.email = email;
        this.uuid = uuid;
    }

   public Map<String, Object> setClaims(){
        return Map.of(
                "username", getUsername(),
                "email", getEmail(),
                "roles", getAuthorities()
        );
    }
}
