package bg.softuni.travelNest.service;

import bg.softuni.travelNest.model.dto.RegisterDto;
import bg.softuni.travelNest.model.entity.User;

import java.util.Optional;

public interface UserService {

    boolean passwordDiff(String password, String confirmPassword);

    boolean register(RegisterDto registerDto);

    User findUser(TravelNestUserDetails travelNestUserDetails);

    Optional<TravelNestUserDetails> getCurrentUser();
}
