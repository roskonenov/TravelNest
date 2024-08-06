package bg.softuni.travelNest.service.impl;

import bg.softuni.travelNest.config.JWTConfig;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class JWTServiceImplTest {

    @Mock
    private JWTConfig jwtConfig;

    @InjectMocks
    private JWTServiceImpl jwtService;

    private static final String SECRET = "testSecretKey123456789012345678901234567890";
    private static final long EXPIRATION = 3600000L;
    private static final String USER_ID = "testUserId";
    private static final Map<String, Object> CLAIMS = new HashMap<>();
    private static final String ROLE = "ROLE_ADMIN";

    @BeforeEach
    void setUp() {
        when(jwtConfig.getSecret()).thenReturn(SECRET);
        when(jwtConfig.getExpiration()).thenReturn(EXPIRATION);
    }

    @Test
    void generateToken() {
        CLAIMS.put("role", ROLE);
        String token = jwtService.generateToken(USER_ID, CLAIMS);
        Key signingKey = Keys.hmacShaKeyFor(SECRET.getBytes(StandardCharsets.UTF_8));
        Claims parsedClaims = Jwts.parserBuilder()
                .setSigningKey(signingKey)
                .build()
                .parseClaimsJws(token)
                .getBody();

        assertEquals(USER_ID, parsedClaims.getSubject());
        assertEquals(ROLE, parsedClaims.get("role"));
        assertEquals(new Date().getTime() + EXPIRATION, parsedClaims.getExpiration().getTime(), 1000);
    }
}