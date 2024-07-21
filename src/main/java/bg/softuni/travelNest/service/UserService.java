package bg.softuni.travelNest.service;

import bg.softuni.travelNest.model.dto.RegisterDto;
import bg.softuni.travelNest.model.entity.User;

import java.util.UUID;

public interface UserService {

    boolean passwordDiff(String password, String confirmPassword);

    boolean register(RegisterDto registerDto);

    User findUser(CurrentUser currentUser);
}
