package bg.softuni.travelNest.init;

import bg.softuni.travelNest.config.InitConfig;
import bg.softuni.travelNest.model.entity.*;
import bg.softuni.travelNest.model.enums.City;
import bg.softuni.travelNest.model.enums.RoleEnum;
import bg.softuni.travelNest.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DatabaseInitTest {

    @Mock
    private CityRepository mockCityRepository;

    @Mock
    private RoleRepository mockRoleRepository;

    @Mock
    private UserRepository mockUserRepository;

    @Mock
    private HousingRepository mockHousingRepository;

    @Mock
    private CarRepository mockCarRepository;

    @Mock
    private InitConfig initConfig;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private DatabaseInit toTest;

    @BeforeEach
    void setUp() {
    }

    @Test
    void testCityInit() {
        when(mockCityRepository.count()).thenReturn(0L);
        when(mockCityRepository.findAll()).thenReturn(List.of());

        toTest.cityInit();

        verify(mockCityRepository, times(City.values().length)).saveAndFlush(any(CityEntity.class));
    }

    @Test
    void testRolesInit() {
        when(mockRoleRepository.count()).thenReturn(0L);
        when(mockRoleRepository.findAll()).thenReturn(List.of());

        toTest.rolesInit();

        verify(mockRoleRepository, times(RoleEnum.values().length)).saveAndFlush(any(Role.class));
    }

    @Test
    void testUsersInit() {
        when(mockUserRepository.count()).thenReturn(0L);
        when(mockRoleRepository.findByRole(RoleEnum.ADMIN)).thenReturn(new Role(RoleEnum.ADMIN));
        when(mockRoleRepository.findByRole(RoleEnum.USER)).thenReturn(new Role(RoleEnum.USER));

        toTest.usersInit();

        verify(mockUserRepository).saveAllAndFlush(anyList());
    }
}