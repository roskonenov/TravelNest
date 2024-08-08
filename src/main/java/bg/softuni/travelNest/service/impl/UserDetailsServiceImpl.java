package bg.softuni.travelNest.service.impl;

import bg.softuni.travelNest.model.entity.Role;
import bg.softuni.travelNest.model.enums.RoleEnum;
import bg.softuni.travelNest.repository.UserRepository;
import bg.softuni.travelNest.model.entity.User;

import bg.softuni.travelNest.service.TravelNestUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username)
                .map(UserDetailsServiceImpl::map)
                .orElseThrow(() -> new UsernameNotFoundException("Username " + username + " not found"));
    }

    private static UserDetails map(User user){
        return new TravelNestUserDetails(
                user.getId(),
                user.getUsername(),
                user.getPassword(),
                user.getRoles().stream().map(Role::getRole).map(UserDetailsServiceImpl::map).toList(),
                user.getEmail()
        );
    }

    private static GrantedAuthority map(RoleEnum role){
        return new SimpleGrantedAuthority("ROLE_" + role);
    }
}
