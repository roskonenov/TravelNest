package bg.softuni.travelNest.service;

import java.util.Map;

public interface JWTService {

    String generateToken(String userId, Map<String, Object> claims);
}
