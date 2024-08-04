package bg.softuni.travelNest.service;

import bg.softuni.travelNest.model.dto.RegisterDto;
import bg.softuni.travelNest.model.entity.User;

import java.util.Optional;

public interface UserService {

    boolean register(RegisterDto registerDto);

    User findUser(TravelNestUserDetails travelNestUserDetails);

    Optional<TravelNestUserDetails> getCurrentUser();
}
