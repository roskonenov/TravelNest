package bg.softuni.travelNest.service.impl;

import bg.softuni.travelNest.data.dto.RegisterDto;
import bg.softuni.travelNest.data.entity.User;
import bg.softuni.travelNest.repository.UserRepository;
import bg.softuni.travelNest.service.UserService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
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
}
