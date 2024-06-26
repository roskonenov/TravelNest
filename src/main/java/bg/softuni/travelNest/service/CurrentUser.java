package bg.softuni.travelNest.service;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;

public class CurrentUser extends User {

    private final String email;

    public CurrentUser(String username,
                       String password,
                       Collection<? extends GrantedAuthority> authorities,
                       String email) {
        super(username, password, authorities);
        this.email = email;
    }

    public String getEmail() {
        return email;
    }
}
