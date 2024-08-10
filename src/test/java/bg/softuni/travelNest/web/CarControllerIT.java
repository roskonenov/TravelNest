package bg.softuni.travelNest.web;

import bg.softuni.travelNest.model.dto.PropertyDTO;
import bg.softuni.travelNest.model.entity.Car;
import bg.softuni.travelNest.model.entity.User;
import bg.softuni.travelNest.model.enums.City;
import bg.softuni.travelNest.model.enums.Engine;
import bg.softuni.travelNest.repository.CarRepository;
import bg.softuni.travelNest.service.PictureService;
import bg.softuni.travelNest.service.TravelNestUserDetails;
import bg.softuni.travelNest.service.UserService;
import bg.softuni.travelNest.service.impl.CarServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;

import static org.junit.jupiter.api.Assertions.*;


import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class CarControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CarRepository carRepository;

    private TravelNestUserDetails userDetails;

    @MockBean
    private CarServiceImpl carService;

    @MockBean
    private UserService userService;

    @MockBean
    private PictureService pictureService;

    @BeforeEach
    void setUp() {
        userDetails = new TravelNestUserDetails(
                UUID.randomUUID(),
                "testUser",
                "password",
                List.of(new SimpleGrantedAuthority("ROLE_USER")),
                "testUser@gmail.com"
        );
    }

    @Test
    void showRentalCars() throws Exception {
        PropertyDTO testCar = new PropertyDTO();
        testCar.setId(UUID.randomUUID());
        testCar.setCity(String.valueOf(City.PLOVDIV));
        testCar.setPictureUrl("testPictureUrl");
        testCar.setAddress("testAddress");
        testCar.setTitle("testTitle");

        when(carService.findAllAdds()).thenReturn(List.of(testCar));

        mockMvc.perform(get("/car/rental")
                        .with(SecurityMockMvcRequestPostProcessors.user(userDetails)))
                .andExpect(model().attributeExists("propertyData"))
                .andExpect(model().attribute("propertyData", hasSize(1)))
                .andExpect(model().attribute("propertyData", hasItem(
                        allOf(
                                hasProperty("id", is(testCar.getId())),
                                hasProperty("city", is(testCar.getCity())),
                                hasProperty("pictureUrl", is(testCar.getPictureUrl())),
                                hasProperty("address", is(testCar.getAddress())),
                                hasProperty("title", is(testCar.getTitle()))
                ))))
                .andExpect(model().attribute("refDetailsLink", is("/car/details/{uuid}")))
                .andExpect(model().attribute("refAddLink", is("/car/add")))
                .andExpect(view().name("property_rental"))
                .andExpect(status().is2xxSuccessful());
    }

    @Test
    void showAddCar() throws Exception {
        mockMvc.perform(get("/car/add")
                .with(SecurityMockMvcRequestPostProcessors.user(userDetails)))
                .andExpect(model().attribute("entityType", "car"))
                .andExpect(model().attribute("action", "/car/add"))
                .andExpect(view().name("add_property"))
                .andExpect(status().is2xxSuccessful());
    }

    @Test
    @Transactional
    void addCar() throws Exception {
        MockMultipartFile file = new MockMultipartFile(
                "image",
                "testImage.jpg",
                "image/jpeg",
                "test image content".getBytes()
        );

        when(pictureService.uploadImage(file)).thenReturn("/testPictureUrl");
        when(userService.findUser(userDetails)).thenReturn(new User());

        mockMvc.perform(multipart("/car/add")
                .file(file)
                .param("address", "testAddress")
                .param("city", City.PLOVDIV.toString())
                .param("price", BigDecimal.TEN.toString())
                .param("make", "testMake")
                .param("model", "testModel")
                .param("engine", Engine.GAS.toString())
                .param("doors", "4")
                .with(csrf())
                .with(SecurityMockMvcRequestPostProcessors.user(userDetails)))
                .andExpect(status().is3xxRedirection());

//        Optional<Car> optionalCar = carRepository.findByAddress("testAddress");
//        assertTrue(optionalCar.isPresent());
//        Car car = optionalCar.get();
//
//        assertEquals(City.PLOVDIV.toString(),car.getCity().getName());
//        assertEquals(BigDecimal.TEN,car.getPrice());
//        assertEquals("testMake",car.getMake());
//        assertEquals("testModel",car.getModel());
//        assertEquals(Engine.GAS,car.getEngine());
//        assertEquals(4,car.getDoors());
    }

    @Test
    void showCarDetails() {
    }

    @Test
    void addComment() {
    }

    @Test
    void addToFavorites() {
    }

    @Test
    void deleteCar() {
    }
}