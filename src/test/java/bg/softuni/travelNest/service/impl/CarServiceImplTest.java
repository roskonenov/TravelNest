package bg.softuni.travelNest.service.impl;

import bg.softuni.travelNest.exception.ObjectNotFoundException;
import bg.softuni.travelNest.model.dto.AddCommentDTO;
import bg.softuni.travelNest.model.dto.AddRentalCarDTO;
import bg.softuni.travelNest.model.dto.CarDetailsDTO;
import bg.softuni.travelNest.model.dto.PropertyDTO;
import bg.softuni.travelNest.model.entity.Car;
import bg.softuni.travelNest.model.entity.CityEntity;
import bg.softuni.travelNest.model.entity.User;
import bg.softuni.travelNest.model.entity.commentEntity.CarComment;
import bg.softuni.travelNest.model.enums.City;
import bg.softuni.travelNest.model.enums.Engine;
import bg.softuni.travelNest.repository.*;
import bg.softuni.travelNest.service.TravelNestUserDetails;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.Pbkdf2PasswordEncoder;

import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CarServiceImplTest {

    private static final UUID USER_ID = UUID.randomUUID();
    private static final String USERNAME = "testUsername";
    private static final String PASSWORD = "testPassword";
    private static final Collection<SimpleGrantedAuthority> AUTHORITIES = List.of(new SimpleGrantedAuthority("ROLE_ADMIN"));
    private static final String EMAIL = "test@test.test";
    private static final UUID CAR_ID = UUID.randomUUID();
    private static final String ADDRESS = "testAddress";
    private static final String CITY = String.valueOf(City.PLOVDIV);
    private static final BigDecimal PRICE = BigDecimal.TEN;
    private static final String PICTURE_URL = "testPictureIrl";
    private static final String MAKE = "testMake";
    private static final String MODEL = "testModel";
    private static final Engine ENGINE = Engine.PETROL;
    private static final Integer DOORS = 4;
    private static final String COMMENT_TYPE = "CAR";
    private static final String COMMENT_TEXT = "testText";

    private CarServiceImpl toTest;

    private TravelNestUserDetails testUserDetails;

    private User testUser;

    @InjectMocks
    private PictureServiceImpl pictureService;

    @InjectMocks
    private UserServiceImpl userService;

    private Car car;

    private AddCommentDTO addCommentDTO;

    @Captor
    private ArgumentCaptor<Car> carArgumentCaptor;

    @Captor
    private ArgumentCaptor<CarComment> commentArgumentCaptor;

    @Mock
    private CityRepository mockCityRepository;

    @Mock
    private CarRepository mockCarRepository;

    @Mock
    private CommentRepository mockCommentRepository;

    @Mock
    private RentRepository mockRentRepository;

    @Mock
    private UserRepository mockUserRepository;

    @Mock
    private RoleRepository roleRepository;

    @BeforeEach
    void setUp() {

        userService = new UserServiceImpl(
                mockUserRepository,
                Pbkdf2PasswordEncoder.defaultsForSpringSecurity_v5_8(),
                roleRepository
        );

        toTest = new CarServiceImpl(
                pictureService,
                userService,
                mockCityRepository,
                mockCarRepository,
                new ModelMapper(),
                mockCommentRepository,
                mockRentRepository
        );

        testUserDetails = new TravelNestUserDetails(USER_ID, USERNAME, PASSWORD, AUTHORITIES, EMAIL);
        testUser = new User(USERNAME, EMAIL, PASSWORD);

        addCommentDTO = new AddCommentDTO();
        addCommentDTO.setText(COMMENT_TEXT);

        car = new Car();
        car.setId(CAR_ID);
        car.setMake(MAKE);
        car.setModel(MODEL);
        car.setEngine(ENGINE);
        car.setDoors(DOORS);
        car.setAddress(ADDRESS);
        car.setCity(new CityEntity(CITY));
        car.setOwner(testUser);
        car.setPictureUrl(PICTURE_URL);
        car.setPrice(PRICE);
    }

    @Test
    @Disabled
    void add() throws IOException {
        AddRentalCarDTO addRentalCarDTO = new AddRentalCarDTO();
        addRentalCarDTO.setAddress(ADDRESS);
        addRentalCarDTO.setCity(CITY);
        addRentalCarDTO.setDoors(DOORS);
        addRentalCarDTO.setEngine(ENGINE);
        addRentalCarDTO.setMake(MAKE);
        addRentalCarDTO.setModel(MODEL);
        addRentalCarDTO.setPrice(PRICE);
        addRentalCarDTO.setImage(new MockMultipartFile(
                "test.xlsx",
                new FileInputStream("F:\\Росен курсове\\Project\\TravelNest\\src\\main\\resources\\static\\images\\about-img.png")));

        when(mockUserRepository.findByUsername(USERNAME))
                .thenReturn(Optional.of(testUser));

        when(mockCityRepository.findByName(CITY))
                .thenReturn(new CityEntity(CITY));

        toTest.add(addRentalCarDTO, testUserDetails);

        verify(mockCarRepository).save(carArgumentCaptor.capture());
        Car actualCar = carArgumentCaptor.getValue();
        assertEquals(addRentalCarDTO.getAddress(), actualCar.getAddress());
        assertEquals(addRentalCarDTO.getCity(), actualCar.getCity().getName());
        assertEquals(addRentalCarDTO.getDoors(), actualCar.getDoors());
        assertEquals(addRentalCarDTO.getEngine(), actualCar.getEngine());
        assertEquals(addRentalCarDTO.getMake(), actualCar.getMake());
        assertEquals(addRentalCarDTO.getModel(), actualCar.getModel());
        assertEquals(addRentalCarDTO.getPrice(), actualCar.getPrice());
        assertEquals("/test-image-url", actualCar.getPictureUrl());
    }

    @Test
    void findDetailsById_Return_CarDetailsDTO() {

        when(mockCarRepository.findById(CAR_ID))
                .thenReturn(Optional.of(car));

        when(mockCommentRepository.findByType(COMMENT_TYPE))
                .thenReturn(new ArrayList<>());

        CarDetailsDTO detailsById = toTest.findDetailsById(CAR_ID);

        assertEquals(CITY, detailsById.getCity());
        assertEquals(ADDRESS, detailsById.getAddress());
        assertEquals(PRICE, detailsById.getPrice());
        assertEquals(PICTURE_URL, detailsById.getPictureUrl());
        assertEquals(MAKE, detailsById.getMake());
        assertEquals(MODEL, detailsById.getModel());
        assertEquals(ENGINE, Engine.valueOf(detailsById.getEngine()));
        assertEquals(DOORS, detailsById.getDoors());
        assertEquals(0, detailsById.getComments().size());
    }

    @Test
    void findDetailsById_Throws(){
        assertThrows(ObjectNotFoundException.class,
                () -> toTest.findDetailsById(CAR_ID));
    }

    @Test
    void findAllAdds_Return_ListOf_PropertyDTO() {
        when(mockCarRepository.findAll())
                .thenReturn(List.of(car, car, car));

        List<PropertyDTO> allAdds = toTest.findAllAdds();

        assertEquals(3, allAdds.size());
        assertEquals(CAR_ID, allAdds.get(0).getId());
        assertEquals(MAKE + " " + MODEL, allAdds.get(1).getTitle());
        assertEquals(CITY, allAdds.get(2).getCity());
        assertEquals(PICTURE_URL, allAdds.get(0).getPictureUrl());
        assertEquals(ADDRESS, allAdds.get(1).getAddress());
    }

    @Test
    void findAllAdds_Return_EmptyList(){
        List<PropertyDTO> allAdds = toTest.findAllAdds();
        assertEquals(0, allAdds.size());
    }

    @Test
    void addComment() {

        when(mockCarRepository.findById(CAR_ID))
                .thenReturn(Optional.of(car));

        toTest.addComment(addCommentDTO, CAR_ID, testUser);

        verify(mockCommentRepository).saveAndFlush(commentArgumentCaptor.capture());
        CarComment carComment = commentArgumentCaptor.getValue();
        assertEquals(COMMENT_TYPE, carComment.getType());
        assertEquals(COMMENT_TEXT, carComment.getText());
        assertEquals(testUser, carComment.getUser());
        assertEquals(car, carComment.getCar());
    }

    @Test
    void addComment_Throw(){
        assertThrows(ObjectNotFoundException.class,
                () -> toTest.addComment(addCommentDTO, CAR_ID, testUser));
    }

    @Test
    void addToFavorites() {
        when(mockCarRepository.findById(CAR_ID))
                .thenReturn(Optional.of(car));

        assertTrue(testUser.getFavoriteCars().isEmpty());

        toTest.addToFavorites(testUser, CAR_ID);

        assertFalse(testUser.getFavoriteCars().isEmpty());
        assertEquals(CAR_ID, testUser.getFavoriteCars().stream().findFirst().get().getId());
        assertEquals(MAKE, testUser.getFavoriteCars().stream().findFirst().get().getMake());
        assertEquals(ADDRESS, testUser.getFavoriteCars().stream().findFirst().get().getAddress());
        assertEquals(CITY, testUser.getFavoriteCars().stream().findFirst().get().getCity().getName());

    }

    @Test
    void addToFavorites_Throws(){
        assertThrows(ObjectNotFoundException.class,
                () -> toTest.addToFavorites(testUser, CAR_ID));
    }

    @Test
    void deleteProperty() {
        when(mockCarRepository.findById(CAR_ID))
                .thenReturn(Optional.of(car));

        toTest.deleteProperty(testUserDetails, CAR_ID);

        verify(mockCommentRepository).deleteAllWhereCar(carArgumentCaptor.capture());
        Car car1 = carArgumentCaptor.getValue();
        assertEquals(car, car1);

        verify(mockRentRepository).deleteAllWhereCar(carArgumentCaptor.capture());
        Car car2 = carArgumentCaptor.getValue();
        assertEquals(car, car2);

        verify(mockCarRepository).delete(carArgumentCaptor.capture());
        Car car3 = carArgumentCaptor.getValue();
        assertEquals(car, car3);
    }

    @Test
    void deleteProperty_Throws(){
        assertThrows(ObjectNotFoundException.class,
                () -> toTest.deleteProperty(testUserDetails, CAR_ID));
    }

    @Test
    void isFavorite() {
        when(mockUserRepository.findByUsername(USERNAME))
                .thenReturn(Optional.of(testUser));

        when(mockCarRepository.findById(CAR_ID))
                .thenReturn(Optional.of(car));

        assertFalse(toTest.isFavorite(testUserDetails, CAR_ID));

        Set<Car> favoriteCars = testUser.getFavoriteCars();
        favoriteCars.add(car);
        testUser.setFavoriteCars(favoriteCars);

        assertTrue(toTest.isFavorite(testUserDetails, CAR_ID));
    }

    @Test
    void findUserFavorites() {
        when(mockCarRepository.findAllByUserFavorites(CAR_ID))
                .thenReturn(Optional.of(List.of(car, car, car)));

        List<PropertyDTO> userFavorites = toTest.findUserFavorites(CAR_ID);
        assertEquals(3, userFavorites.size());
        assertEquals(CAR_ID, userFavorites.getFirst().getId());
        assertEquals(MAKE + " " + MODEL, userFavorites.get(1).getTitle());
        assertEquals(CITY, userFavorites.get(2).getCity());
        assertEquals(PICTURE_URL, userFavorites.getLast().getPictureUrl());
        assertEquals(ADDRESS, userFavorites.get(1).getAddress());
    }

    @Test
    void findUserFavorites_Return_EmptyList(){
        List<PropertyDTO> userFavorites = toTest.findUserFavorites(CAR_ID);
        assertTrue(userFavorites.isEmpty());
    }
}