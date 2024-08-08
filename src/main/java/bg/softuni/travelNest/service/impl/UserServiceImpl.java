package bg.softuni.travelNest.service.impl;

import bg.softuni.travelNest.exception.ObjectNotFoundException;
import bg.softuni.travelNest.model.dto.RegisterDto;
import bg.softuni.travelNest.model.entity.User;
import bg.softuni.travelNest.model.enums.RoleEnum;
import bg.softuni.travelNest.repository.RoleRepository;
import bg.softuni.travelNest.repository.UserRepository;
import bg.softuni.travelNest.service.TravelNestUserDetails;
import bg.softuni.travelNest.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;

    @Override
    public boolean register(RegisterDto registerDto) {
        if (userRepository.existsByUsernameOrEmail(registerDto.getUsername(), registerDto.getEmail())) return false;

        userRepository.saveAndFlush(
                new User()
                        .setUsername(registerDto.getUsername())
                        .setEmail(registerDto.getEmail())
                        .setPassword(passwordEncoder.encode(registerDto.getPassword()))
                        .setRoles(List.of(
                                userRepository.count() == 0
                                ?       roleRepository.findByRole(RoleEnum.ADMIN)
                                :       roleRepository.findByRole(RoleEnum.USER)
                        ))
        );
        return true;
    }

    @Override
    public User findUser(TravelNestUserDetails travelNestUserDetails) {
        return userRepository.findByUsername(travelNestUserDetails.getUsername())
                .orElseThrow(() -> new ObjectNotFoundException("Current user not found"));
    }

    @Override
    public Optional<TravelNestUserDetails> getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null &&
                authentication.getPrincipal() instanceof TravelNestUserDetails travelNestUserDetails) {
            return Optional.of(travelNestUserDetails);
        }
        return Optional.empty();
    }
}
