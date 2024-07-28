package bg.softuni.travelNest.service.impl;

import bg.softuni.travelNest.config.JWTConfig;
import bg.softuni.travelNest.service.JWTService;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class JWTServiceImpl implements JWTService {

    private final JWTConfig jwtConfig;

    @Override
    public String generateToken(String userId, Map<String, Object> claims) {
        Date now = new Date();

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(userId)
                .setIssuedAt(now)
                .setNotBefore(now)
                .setExpiration(new Date(now.getTime() + jwtConfig.getExpiration()))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    private Key getSigningKey(){
        return Keys
                .hmacShaKeyFor(jwtConfig
                        .getSecret()
                        .getBytes(StandardCharsets.UTF_8));
    }
}
