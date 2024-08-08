package bg.softuni.travelNest.service.impl;

import bg.softuni.travelNest.exception.ObjectNotFoundException;
import bg.softuni.travelNest.model.dto.AddCommentDTO;
import bg.softuni.travelNest.model.dto.CarDetailsDTO;
import bg.softuni.travelNest.model.dto.HousingDetailsDTO;
import bg.softuni.travelNest.model.dto.PropertyDTO;
import bg.softuni.travelNest.model.entity.Car;
import bg.softuni.travelNest.model.entity.CityEntity;
import bg.softuni.travelNest.model.entity.Housing;
import bg.softuni.travelNest.model.entity.User;
import bg.softuni.travelNest.model.entity.commentEntity.CarComment;
import bg.softuni.travelNest.model.entity.commentEntity.HousingComment;
import bg.softuni.travelNest.model.enums.City;
import bg.softuni.travelNest.model.enums.Engine;
import bg.softuni.travelNest.model.enums.HousingType;
import bg.softuni.travelNest.repository.*;
import bg.softuni.travelNest.service.PictureService;
import bg.softuni.travelNest.service.TravelNestUserDetails;
import bg.softuni.travelNest.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.Pbkdf2PasswordEncoder;

import java.math.BigDecimal;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
@ExtendWith(MockitoExtension.class)
class HousingServiceImplTest {

    private static final UUID USER_ID = UUID.randomUUID();
    private static final String USERNAME = "testUsername";
    private static final String PASSWORD = "testPassword";
    private static final Collection<SimpleGrantedAuthority> AUTHORITIES = List.of(new SimpleGrantedAuthority("ROLE_ADMIN"));
    private static final String EMAIL = "test@test.test";
    private static final UUID HOUSING_ID = UUID.randomUUID();
    private static final String HOUSING_TYPE = String.valueOf(HousingType.HOUSE);
    private static final String ADDRESS = "testAddress";
    private static final String CITY = String.valueOf(City.PLOVDIV);
    private static final BigDecimal PRICE = BigDecimal.TEN;
    private static final String PICTURE_URL = "testPictureIrl";
    private static final Integer FLOOR = 2;
    private static final Integer ROOMS = 3;
    private static final String COMMENT_TYPE = "HOUSING";
    private static final String COMMENT_TEXT = "testText";

    private HousingServiceImpl toTest;

    private TravelNestUserDetails testUserDetails;

    private User testUser;

    private Housing housing;

    private AddCommentDTO addCommentDTO;

    @Mock
    private PictureService pictureService;

    @Mock
    private UserService userService;

    @Mock
    private CityRepository mockCityRepository;

    @Mock
    private HousingRepository mockHousingRepository;

    @Mock
    private CommentRepository mockCommentRepository;

    @Mock
    private RentRepository mockRentRepository;

    @Mock
    private UserRepository mockUserRepository;

    @Mock
    private RoleRepository roleRepository;

    @Captor
    private ArgumentCaptor<Housing> housingArgumentCaptor;

    @Captor
    private ArgumentCaptor<HousingComment> commentArgumentCaptor;

    @BeforeEach
    void setUp() {
        userService = new UserServiceImpl(
                mockUserRepository,
                Pbkdf2PasswordEncoder.defaultsForSpringSecurity_v5_8(),
                roleRepository
        );

        toTest = new HousingServiceImpl(
                mockHousingRepository,
                new ModelMapper(),
                pictureService,
                userService,
                mockCityRepository,
                mockCommentRepository,
                mockRentRepository
        );

        testUserDetails = new TravelNestUserDetails(USER_ID, USERNAME, PASSWORD, AUTHORITIES, EMAIL);
        testUser = new User(USERNAME, EMAIL, PASSWORD);

        addCommentDTO = new AddCommentDTO();
        addCommentDTO.setText(COMMENT_TEXT);

        housing = new Housing();
        housing.setId(HOUSING_ID);
        housing.setType(HousingType.valueOf(HOUSING_TYPE.toUpperCase()));
        housing.setFloor(FLOOR);
        housing.setRooms(ROOMS);
        housing.setAddress(ADDRESS);
        housing.setCity(new CityEntity(CITY));
        housing.setOwner(testUser);
        housing.setPictureUrl(PICTURE_URL);
        housing.setPrice(PRICE);
    }

    @Test
    void add() {
    }

    @Test
    void findDetailsById_Return_HousingDetailsDTO() {
        when(mockHousingRepository.findById(HOUSING_ID))
                .thenReturn(Optional.of(housing));

        when(mockCommentRepository.findByType(COMMENT_TYPE))
                .thenReturn(new ArrayList<>());

        HousingDetailsDTO detailsById = toTest.findDetailsById(HOUSING_ID);

        assertEquals(CITY, detailsById.getCity());
        assertEquals(ADDRESS, detailsById.getAddress());
        assertEquals(PRICE, detailsById.getPrice());
        assertEquals(PICTURE_URL, detailsById.getPictureUrl());
        assertEquals(FLOOR, detailsById.getFloor());
        assertEquals(ROOMS, detailsById.getRooms());
        assertEquals(0, detailsById.getComments().size());
    }

    @Test
    void findDetailsById_Throws(){
        assertThrows(ObjectNotFoundException.class,
                () -> toTest.findDetailsById(HOUSING_ID));
    }

    @Test
    void findAllAdds_Return_ListOf_PropertyDTO() {
        when(mockHousingRepository.findAll())
                .thenReturn(List.of(housing, housing, housing));

        List<PropertyDTO> allAdds = toTest.findAllAdds();

        assertEquals(3, allAdds.size());
        assertEquals(HOUSING_ID, allAdds.get(0).getId());
        assertEquals(HOUSING_TYPE.toLowerCase() + " " +
                ROOMS + " rooms", allAdds.get(1).getTitle());
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

        when(mockHousingRepository.findById(HOUSING_ID))
                .thenReturn(Optional.of(housing));

        toTest.addComment(addCommentDTO, HOUSING_ID, testUser);

        verify(mockCommentRepository).saveAndFlush(commentArgumentCaptor.capture());
        HousingComment housingComment = commentArgumentCaptor.getValue();
        assertEquals(COMMENT_TYPE, housingComment.getType());
        assertEquals(COMMENT_TEXT, housingComment.getText());
        assertEquals(testUser, housingComment.getUser());
        assertEquals(housing, housingComment.getHousing());
    }

    @Test
    void addComment_Throw(){
        assertThrows(ObjectNotFoundException.class,
                () -> toTest.addComment(addCommentDTO, HOUSING_ID, testUser));
    }

    @Test
    void addToFavorites() {
        when(mockHousingRepository.findById(HOUSING_ID))
                .thenReturn(Optional.of(housing));

        assertTrue(testUser.getFavoriteCars().isEmpty());

        toTest.addToFavorites(testUser, HOUSING_ID);

        assertFalse(testUser.getFavoriteHousings().isEmpty());
        assertEquals(HOUSING_ID, testUser.getFavoriteHousings().stream().findFirst().get().getId());
        assertEquals(PRICE, testUser.getFavoriteHousings().stream().findFirst().get().getPrice());
        assertEquals(HOUSING_TYPE, testUser.getFavoriteHousings().stream().findFirst().get().getType().toString());
        assertEquals(FLOOR, testUser.getFavoriteHousings().stream().findFirst().get().getFloor());
        assertEquals(ADDRESS, testUser.getFavoriteHousings().stream().findFirst().get().getAddress());
        assertEquals(CITY, testUser.getFavoriteHousings().stream().findFirst().get().getCity().getName());

    }

    @Test
    void addToFavorites_Throws(){
        assertThrows(ObjectNotFoundException.class,
                () -> toTest.addToFavorites(testUser, HOUSING_ID));
    }

    @Test
    void deleteProperty() {
        when(mockHousingRepository.findById(HOUSING_ID))
                .thenReturn(Optional.of(housing));

        toTest.deleteProperty(testUserDetails, HOUSING_ID);

        verify(mockCommentRepository).deleteAllWhereHousing(housingArgumentCaptor.capture());
        Housing housing1 = housingArgumentCaptor.getValue();
        assertEquals(housing, housing1);

        verify(mockRentRepository).deleteAllWhereHousing(housingArgumentCaptor.capture());
        Housing housing2 = housingArgumentCaptor.getValue();
        assertEquals(housing, housing2);

        verify(mockHousingRepository).delete(housingArgumentCaptor.capture());
        Housing housing3 = housingArgumentCaptor.getValue();
        assertEquals(housing, housing3);
    }

    @Test
    void deleteProperty_Throws(){
        assertThrows(ObjectNotFoundException.class,
                () -> toTest.deleteProperty(testUserDetails, HOUSING_ID));
    }

    @Test
    void isFavorite() {
        when(mockUserRepository.findByUsername(USERNAME))
                .thenReturn(Optional.of(testUser));

        when(mockHousingRepository.findById(HOUSING_ID))
                .thenReturn(Optional.of(housing));

        assertFalse(toTest.isFavorite(testUserDetails, HOUSING_ID));

        Set<Housing> favoriteHousings = testUser.getFavoriteHousings();
        favoriteHousings.add(housing);
        testUser.setFavoriteHousings(favoriteHousings);

        assertTrue(toTest.isFavorite(testUserDetails, HOUSING_ID));
    }

    @Test
    void findUserFavorites() {
        when(mockHousingRepository.findAllByUserFavorites(HOUSING_ID))
                .thenReturn(Optional.of(List.of(housing, housing, housing)));

        List<PropertyDTO> userFavorites = toTest.findUserFavorites(HOUSING_ID);
        assertEquals(3, userFavorites.size());
        assertEquals(HOUSING_ID, userFavorites.getFirst().getId());
        assertEquals(HOUSING_TYPE.toLowerCase() + " " +
                ROOMS + " rooms", userFavorites.get(1).getTitle());
        assertEquals(CITY, userFavorites.get(2).getCity());
        assertEquals(PICTURE_URL, userFavorites.getLast().getPictureUrl());
        assertEquals(ADDRESS, userFavorites.get(1).getAddress());
    }

    @Test
    void findUserFavorites_Return_EmptyList(){
        List<PropertyDTO> userFavorites = toTest.findUserFavorites(HOUSING_ID);
        assertTrue(userFavorites.isEmpty());
    }
}