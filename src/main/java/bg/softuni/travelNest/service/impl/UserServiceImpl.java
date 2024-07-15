package bg.softuni.travelNest.service.impl;

import bg.softuni.travelNest.exception.ObjectNotFoundException;
import bg.softuni.travelNest.model.dto.RegisterDto;
import bg.softuni.travelNest.model.entity.Housing;
import bg.softuni.travelNest.model.entity.User;
import bg.softuni.travelNest.repository.HousingRepository;
import bg.softuni.travelNest.repository.UserRepository;
import bg.softuni.travelNest.service.CurrentUser;
import bg.softuni.travelNest.service.UserService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final HousingRepository housingRepository;

    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder, HousingRepository housingRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.housingRepository = housingRepository;
    }

    @Override
    public boolean passwordDiff(String password, String confirmPassword) {
        return !password.equals(confirmPassword);
    }

    @Override
    public boolean register(RegisterDto registerDto) {
        if (userRepository.existsByUsernameOrEmail(registerDto.getUsername(), registerDto.getEmail())) return false;

        userRepository.saveAndFlush(
                new User()
                        .setUsername(registerDto.getUsername())
                        .setEmail(registerDto.getEmail())
                        .setPassword(passwordEncoder.encode(registerDto.getPassword()))
        );return true;
    }

    @Override
    public User findUser(CurrentUser currentUser) {
        return userRepository.findByUsername(currentUser.getUsername())
                .orElseThrow(() -> new ObjectNotFoundException("Current user not found"));
    }

    @Override
    public boolean isFavorite(CurrentUser currentUser, UUID id) {
        return this.findUser(currentUser)
                .getFavorites()
                .contains(housingRepository.findById(id).orElse(new Housing()));

    }
}
