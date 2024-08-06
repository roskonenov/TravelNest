package bg.softuni.travelNest.service.impl;

import bg.softuni.travelNest.model.entity.Role;
import bg.softuni.travelNest.model.entity.User;
import bg.softuni.travelNest.model.enums.RoleEnum;
import bg.softuni.travelNest.repository.UserRepository;
import bg.softuni.travelNest.service.TravelNestUserDetails;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserDetailsServiceImplTest {

    private static final UUID USER_ID = UUID.randomUUID();
    private static final String USERNAME = "testUsername";
    private static final String PASSWORD = "testPassword";
    private static final String EMAIL = "test@test.com";
    private static final Role ROLE = new Role(RoleEnum.USER);

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserDetailsServiceImpl userDetailsService;

    private User testUser;

    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setId(USER_ID);
        testUser.setUsername(USERNAME);
        testUser.setPassword(PASSWORD);
        testUser.setEmail(EMAIL);
        testUser.setRoles(List.of(ROLE));
    }

    @Test
    void loadUserByUsername_UserExists() {
        when(userRepository.findByUsername(USERNAME)).thenReturn(Optional.of(testUser));

        TravelNestUserDetails userDetails = (TravelNestUserDetails) userDetailsService.loadUserByUsername(USERNAME);

        assertNotNull(userDetails);
        assertEquals(USER_ID, userDetails.getUuid());
        assertEquals(USERNAME, userDetails.getUsername());
        assertEquals(PASSWORD, userDetails.getPassword());
        assertEquals(EMAIL, userDetails.getEmail());
        assertEquals(1, userDetails.getAuthorities().size());
        assertEquals("ROLE_" + ROLE.getRole().name(), userDetails.getAuthorities().iterator().next().getAuthority());
    }

    @Test
    void loadUserByUsername_UserDoesNotExist() {
        when(userRepository.findByUsername("NOT_EXISTING_USER")).thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class,
                () -> userDetailsService.loadUserByUsername("NOT_EXISTING_USER"));
    }
}