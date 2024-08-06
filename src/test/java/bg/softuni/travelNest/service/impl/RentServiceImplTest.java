package bg.softuni.travelNest.service.impl;

import bg.softuni.travelNest.exception.ObjectNotFoundException;
import bg.softuni.travelNest.model.dto.RentDTO;
import bg.softuni.travelNest.model.entity.Car;
import bg.softuni.travelNest.model.entity.CityEntity;
import bg.softuni.travelNest.model.entity.Housing;
import bg.softuni.travelNest.model.entity.User;
import bg.softuni.travelNest.model.entity.rentPeriodEntity.CarRentPeriod;
import bg.softuni.travelNest.model.entity.rentPeriodEntity.HousingRentPeriod;
import bg.softuni.travelNest.model.enums.City;
import bg.softuni.travelNest.model.enums.Engine;
import bg.softuni.travelNest.model.enums.HousingType;
import bg.softuni.travelNest.repository.CarRepository;
import bg.softuni.travelNest.repository.HousingRepository;
import bg.softuni.travelNest.repository.RentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RentServiceImplTest {

    private static final UUID USER_ID = UUID.randomUUID();
    private static final String USERNAME = "testUsername";
    private static final String PASSWORD = "testPassword";
    private static final Collection<SimpleGrantedAuthority> AUTHORITIES = List.of(new SimpleGrantedAuthority("ROLE_ADMIN"));
    private static final String EMAIL = "test@test.test";
    private static final UUID PROPERTY_ID = UUID.randomUUID();
    private static final String HOUSING_TYPE = String.valueOf(HousingType.HOUSE);
    private static final String ADDRESS = "testAddress";
    private static final String CITY = String.valueOf(City.PLOVDIV);
    private static final BigDecimal PRICE = BigDecimal.TEN;
    private static final String PICTURE_URL = "testPictureIrl";
    private static final Integer FLOOR = 2;
    private static final Integer ROOMS = 3;
    private static final String MAKE = "testMake";
    private static final String MODEL = "testModel";
    private static final Engine ENGINE = Engine.PETROL;
    private static final Integer DOORS = 4;
    private static final LocalDate START_DATE = LocalDate.of(2024, 8, 10);
    private static final LocalDate END_DATE = START_DATE.plusDays(5);
    private static final String ENTITY_HOUSING_TYPE = "housing";
    private static final String ENTITY_CAR_TYPE = "car";

    private RentServiceImpl toTest;

    @Mock
    private HousingRepository mockHousingRepository;

    @Mock
    private RentRepository mockRentRepository;

    @Mock
    private CarRepository mockCarRepository;

    private Housing housing;

    private Car car;

    private User testUser;

    private HousingRentPeriod testHousingRentPeriod;

    private CarRentPeriod testCarRentPeriod;

    private List<HousingRentPeriod> testListOfHousingRentPeriods;

    private List<CarRentPeriod> testListOfCarRentPeriods;

    @BeforeEach
    void setUp() {

        toTest = new RentServiceImpl (
                mockRentRepository,
                mockHousingRepository,
                mockCarRepository
        );

        housing = new Housing();
        housing.setId(PROPERTY_ID);
        housing.setType(HousingType.valueOf(HOUSING_TYPE.toUpperCase()));
        housing.setFloor(FLOOR);
        housing.setRooms(ROOMS);
        housing.setAddress(ADDRESS);
        housing.setCity(new CityEntity(CITY));
        housing.setOwner(testUser);
        housing.setPictureUrl(PICTURE_URL);
        housing.setPrice(PRICE);

        car = new Car();
        car.setId(PROPERTY_ID);
        car.setMake(MAKE);
        car.setModel(MODEL);
        car.setEngine(ENGINE);
        car.setDoors(DOORS);
        car.setAddress(ADDRESS);
        car.setCity(new CityEntity(CITY));
        car.setOwner(testUser);
        car.setPictureUrl(PICTURE_URL);
        car.setPrice(PRICE);

        testUser = new User(USERNAME, EMAIL, PASSWORD);

        testHousingRentPeriod = new HousingRentPeriod(USER_ID, START_DATE, END_DATE, housing);
        HousingRentPeriod testHousingRentPeriod2 = new HousingRentPeriod(USER_ID, START_DATE.plusDays(5), END_DATE.plusDays(5), housing);
        HousingRentPeriod testHousingRentPeriod3 = new HousingRentPeriod(USER_ID, START_DATE.plusDays(10), END_DATE.plusDays(10), housing);

        testListOfHousingRentPeriods = List.of(testHousingRentPeriod, testHousingRentPeriod2, testHousingRentPeriod3);

        testCarRentPeriod = new CarRentPeriod(USER_ID, START_DATE, END_DATE, car);
        CarRentPeriod testCarRentPeriod2 = new CarRentPeriod(USER_ID, START_DATE.plusDays(5), END_DATE.plusDays(5), car);
        CarRentPeriod testCarRentPeriod3 = new CarRentPeriod(USER_ID, START_DATE.plusDays(10), END_DATE.plusDays(10), car);

        testListOfCarRentPeriods = List.of(testCarRentPeriod, testCarRentPeriod2, testCarRentPeriod3);
    }


    @Test
    void getHousingRentPeriods() {
        when(mockHousingRepository.findById(PROPERTY_ID))
                .thenReturn(Optional.of(housing));

        List<HousingRentPeriod> rentPeriods = toTest.getHousingRentPeriods(PROPERTY_ID);

        assertTrue(rentPeriods.isEmpty());

        housing.setRentPeriods(testListOfHousingRentPeriods);

        rentPeriods = toTest.getHousingRentPeriods(PROPERTY_ID);

        assertFalse(rentPeriods.isEmpty());
        assertInstanceOf(HousingRentPeriod.class, rentPeriods.getFirst());
        assertEquals(START_DATE, rentPeriods.getFirst().getStartDate());
        assertEquals(START_DATE.plusDays(5), rentPeriods.get(1).getStartDate());
        assertEquals(START_DATE.plusDays(10), rentPeriods.getLast().getStartDate());
        assertEquals(END_DATE, rentPeriods.getFirst().getEndDate());
        assertEquals(END_DATE.plusDays(5), rentPeriods.get(1).getEndDate());
        assertEquals(END_DATE.plusDays(10), rentPeriods.getLast().getEndDate());
    }

    @Test
    void getHousingRentPeriods_Throws(){
        assertThrows(ObjectNotFoundException.class,
                () -> toTest.getHousingRentPeriods(PROPERTY_ID));
    }

    @Test
    void getCarRentPeriods() {
        when(mockCarRepository.findById(PROPERTY_ID))
                .thenReturn(Optional.of(car));

        List<CarRentPeriod> rentPeriods = toTest.getCarRentPeriods(PROPERTY_ID);

        assertTrue(rentPeriods.isEmpty());

        car.setRentPeriods(testListOfCarRentPeriods);

        rentPeriods = toTest.getCarRentPeriods(PROPERTY_ID);

        assertFalse(rentPeriods.isEmpty());
        assertInstanceOf(CarRentPeriod.class, rentPeriods.getFirst());
        assertEquals(START_DATE, rentPeriods.getFirst().getStartDate());
        assertEquals(START_DATE.plusDays(5), rentPeriods.get(1).getStartDate());
        assertEquals(START_DATE.plusDays(10), rentPeriods.getLast().getStartDate());
        assertEquals(END_DATE, rentPeriods.getFirst().getEndDate());
        assertEquals(END_DATE.plusDays(5), rentPeriods.get(1).getEndDate());
        assertEquals(END_DATE.plusDays(10), rentPeriods.getLast().getEndDate());
    }

    @Test
    void getCarRentPeriods_Throws(){
        assertThrows(ObjectNotFoundException.class,
                () -> toTest.getCarRentPeriods(PROPERTY_ID));
    }

    @Test
    void isAvailable() {
        when(mockHousingRepository.findById(PROPERTY_ID))
                .thenReturn(Optional.of(housing));

        when(mockCarRepository.findById(PROPERTY_ID))
                .thenReturn(Optional.of(car));

        housing.setRentPeriods(List.of(testHousingRentPeriod));

        testCarRentPeriod.setStartDate(START_DATE.plusDays(5));
        testCarRentPeriod.setStartDate(END_DATE.plusDays(5));
        car.setRentPeriods(List.of(testCarRentPeriod));

        assertTrue(toTest.isAvailable(
                ENTITY_HOUSING_TYPE,
                PROPERTY_ID,
                START_DATE.plusDays(5),
                END_DATE.plusDays(5))
        );
        assertFalse(toTest.isAvailable(
                ENTITY_HOUSING_TYPE,
                PROPERTY_ID,
                START_DATE.plusDays(2),
                END_DATE.plusDays(8))
        );
        assertFalse(toTest.isAvailable(
                ENTITY_HOUSING_TYPE,
                PROPERTY_ID,
                START_DATE.minusDays(5),
                END_DATE.plusDays(3))
        );
        assertTrue(toTest.isAvailable(
                ENTITY_CAR_TYPE,
                PROPERTY_ID,
                START_DATE.plusDays(6),
                END_DATE.plusDays(8))
        );
        assertFalse(toTest.isAvailable(
                ENTITY_CAR_TYPE,
                PROPERTY_ID,
                START_DATE.plusDays(4),
                END_DATE.plusDays(8))
        );
        assertFalse(toTest.isAvailable(
                ENTITY_CAR_TYPE,
                PROPERTY_ID,
                START_DATE.plusDays(3),
                END_DATE.plusDays(7))
        );
    }

    @Test
    void isAvailable_Throws_When_PropertyType_IsWrong(){
        assertThrows(ObjectNotFoundException.class,
                () -> toTest.isAvailable("NON_EXISTING_TYPE", PROPERTY_ID, START_DATE, END_DATE));
    }

    @Test
    void isAvailable_Throws_When_PropertyId_NonExisting(){
        assertThrows(ObjectNotFoundException.class,
                () -> toTest.isAvailable(ENTITY_CAR_TYPE, PROPERTY_ID, START_DATE, END_DATE));

        assertThrows(ObjectNotFoundException.class,
                () -> toTest.isAvailable(ENTITY_HOUSING_TYPE, PROPERTY_ID, START_DATE, END_DATE));
    }

    @Test
    void rent_Returns_Proper_Message() {
        RentDTO rentDTO = new RentDTO();
        rentDTO.setStartDate(START_DATE);
        rentDTO.setEndDate(END_DATE);
        rentDTO.setId(PROPERTY_ID);
        rentDTO.setRenter(testUser);

        when(mockHousingRepository.findById(PROPERTY_ID))
                .thenReturn(Optional.of(housing));
        when(mockCarRepository.findById(PROPERTY_ID))
                .thenReturn(Optional.of(car));

        assertEquals("Failed to rent the property!",
                toTest.rent(rentDTO, "NON_EXISTING_PROPERTY_TYPE"));
        assertEquals("The housing was rented successfully!",
                toTest.rent(rentDTO, ENTITY_HOUSING_TYPE));
        assertEquals("The car was rented successfully!",
                toTest.rent(rentDTO, ENTITY_CAR_TYPE));

        housing.setRentPeriods(List.of(testHousingRentPeriod));
        car.setRentPeriods(List.of(testCarRentPeriod));

        assertEquals("The housing is not available during the selected period!",
                toTest.rent(rentDTO, ENTITY_HOUSING_TYPE));
        assertEquals("The car is not available during the selected period!",
                toTest.rent(rentDTO, ENTITY_CAR_TYPE));
    }

    @Test
    void rent_Returns_ProperMessage_When_PropertyId_NotExisting(){
        RentDTO rentDTO = new RentDTO();
        rentDTO.setStartDate(START_DATE);
        rentDTO.setEndDate(END_DATE);
        rentDTO.setId(PROPERTY_ID);
        rentDTO.setRenter(testUser);

        assertEquals("Failed to rent the property!",
                toTest.rent(rentDTO, ENTITY_HOUSING_TYPE));
        assertEquals("Failed to rent the property!",
                toTest.rent(rentDTO, ENTITY_CAR_TYPE));
    }

}