package bg.softuni.travelNest.service.impl;

import bg.softuni.travelNest.exception.ObjectNotFoundException;
import bg.softuni.travelNest.model.dto.RegisterDto;
import bg.softuni.travelNest.model.entity.Role;
import bg.softuni.travelNest.model.entity.User;
import bg.softuni.travelNest.model.enums.RoleEnum;
import bg.softuni.travelNest.repository.RoleRepository;
import bg.softuni.travelNest.repository.UserRepository;
import bg.softuni.travelNest.service.TravelNestUserDetails;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    private final static String USERNAME = "testUsername";
    private final static String PASSWORD = "testPassword";
    private final static String EMAIL = "test@email.com";
    private final static Role ROLE = new Role(RoleEnum.USER);

    @Mock
    private UserRepository mockUserRepository;

    @Mock
    private RoleRepository mockRoleRepository;

    @Mock
    private PasswordEncoder mockPasswordEncoder;

    @InjectMocks
    private UserServiceImpl mockUserService;

    private RegisterDto registerDto;

    @BeforeEach
    void setUp() {
        registerDto = new RegisterDto();
        registerDto.setUsername(USERNAME);
        registerDto.setEmail(EMAIL);
        registerDto.setPassword(PASSWORD);
    }

    @Test
    void testRegisterUserAlreadyExists() {
        when(mockUserRepository.existsByUsernameOrEmail(USERNAME, EMAIL))
                .thenReturn(true);

        assertFalse(mockUserService.register(registerDto));
        verify(mockUserRepository, never()).saveAndFlush(any(User.class));
    }

    @Test
    void testRegisterNewUser() {
        when(mockUserRepository.existsByUsernameOrEmail(USERNAME, EMAIL))
                .thenReturn(false);
        when(mockUserRepository.count())
                .thenReturn(1L);
        when(mockPasswordEncoder.encode(PASSWORD))
                .thenReturn(PASSWORD + PASSWORD);
        when(mockRoleRepository.findByRole(ROLE.getRole()))
                .thenReturn(ROLE);

        assertTrue(mockUserService.register(registerDto));
        verify(mockUserRepository, times(1)).saveAndFlush(any(User.class));
    }

    @Test
    void testFindUser() {
        TravelNestUserDetails userDetails = mock(TravelNestUserDetails.class);
        when(userDetails.getUsername())
                .thenReturn(USERNAME);
        User user = new User();
        when(mockUserRepository.findByUsername(USERNAME))
                .thenReturn(Optional.of(user));

        assertEquals(user, mockUserService.findUser(userDetails));
    }

    @Test
    void testFindUserNotFound() {
        TravelNestUserDetails userDetails = mock(TravelNestUserDetails.class);
        when(userDetails.getUsername())
                .thenReturn(USERNAME);
        when(mockUserRepository.findByUsername(USERNAME))
                .thenReturn(Optional.empty());

        assertThrows(ObjectNotFoundException.class,
                () -> mockUserService.findUser(userDetails));
    }

    @Test
    void testGetCurrentUser() {
        Authentication authentication = mock(Authentication.class);
        SecurityContext securityContext = mock(SecurityContext.class);
        TravelNestUserDetails userDetails = mock(TravelNestUserDetails.class);

        when(securityContext.getAuthentication())
                .thenReturn(authentication);
        when(authentication.getPrincipal())
                .thenReturn(userDetails);

        SecurityContextHolder.setContext(securityContext);

        assertTrue(mockUserService.getCurrentUser().isPresent());
        assertEquals(userDetails, mockUserService.getCurrentUser().get());
    }

    @Test
    void testGetCurrentUserNoAuthentication() {
        SecurityContext securityContext = mock(SecurityContext.class);

        when(securityContext.getAuthentication())
                .thenReturn(null);

        SecurityContextHolder.setContext(securityContext);

        assertFalse(mockUserService.getCurrentUser().isPresent());
    }

    @Test
    void testGetCurrentUserInvalidPrincipal() {
        Authentication authentication = mock(Authentication.class);
        SecurityContext securityContext = mock(SecurityContext.class);

        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(new Object());

        SecurityContextHolder.setContext(securityContext);

        assertFalse(mockUserService.getCurrentUser().isPresent());
    }
}