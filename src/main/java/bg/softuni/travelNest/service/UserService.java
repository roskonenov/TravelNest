package bg.softuni.travelNest.service;

import bg.softuni.travelNest.data.dto.RegisterDto;

public interface UserService {

    boolean passwordDiff(String password, String confirmPassword);

    boolean register(RegisterDto registerDto);
}
