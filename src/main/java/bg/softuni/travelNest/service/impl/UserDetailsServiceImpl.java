package bg.softuni.travelNest.service.impl;

import bg.softuni.travelNest.repository.UserRepository;
import bg.softuni.travelNest.model.entity.User;

import bg.softuni.travelNest.service.CurrentUser;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.ArrayList;

public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    public UserDetailsServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username)
                .map(UserDetailsServiceImpl::map)
                .orElseThrow(() -> new UsernameNotFoundException("Username " + username + " not found"));
    }

    private static UserDetails map(User user){
        return new CurrentUser(
                user.getUsername(),
                user.getPassword(),
                new ArrayList<>(),
                user.getEmail()
        );
    }
}
