package bg.softuni.travelNest.service;

import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;

@Getter
public class CurrentUser extends User {

    private final String email;

    public CurrentUser(String username,
                       String password,
                       Collection<? extends GrantedAuthority> authorities,
                       String email) {
        super(username, password, authorities);
        this.email = email;
    }
}
