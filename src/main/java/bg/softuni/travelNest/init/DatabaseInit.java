package bg.softuni.travelNest.init;

import bg.softuni.travelNest.config.InitConfig;
import bg.softuni.travelNest.model.entity.*;
import bg.softuni.travelNest.model.enums.City;
import bg.softuni.travelNest.model.enums.Engine;
import bg.softuni.travelNest.model.enums.HousingType;
import bg.softuni.travelNest.model.enums.RoleEnum;
import bg.softuni.travelNest.repository.*;
import lombok.RequiredArgsConstructor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;

@Component
@RequiredArgsConstructor
public class DatabaseInit implements CommandLineRunner {

    private static final String HOUSING_INPUT_FILE_PATH = "src/main/resources/static/housing.txt";
    
    private static final String CAR_INPUT_FILE_PATH = "src/main/resources/static/car.txt";
    
    private static final Logger LOGGER = LoggerFactory.getLogger(DatabaseInit.class);

    private final CityRepository cityRepository;
    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final HousingRepository housingRepository;
    private final PasswordEncoder passwordEncoder;
    private final InitConfig initConfig;
    private final CarRepository carRepository;

    @Override
    public void run(String... args) throws Exception {
        cityInit();
        rolesInit();
        usersInit();
        housingInit();
        carInit();
        LOGGER.info("DATABASE INITIATED!");
    }

    private void cityInit() {
        if (cityRepository.count() != Arrays.stream(City.values()).count()) {

            Arrays.stream(City.values())
                    .map(City::toString)
                    .filter(cityName -> !cityRepository.findAll()
                            .stream()
                            .map(CityEntity::getName)
                            .toList().contains(cityName))
                    .forEach(newCity -> cityRepository.saveAndFlush(new CityEntity(newCity)) );
        }
    }

    private void rolesInit() {
        if (roleRepository.count() != Arrays.stream(RoleEnum.values()).count()) {

                    Arrays.stream(RoleEnum.values())
                            .map(RoleEnum::toString)
                            .filter(role -> !roleRepository.findAll()
                                    .stream()
                                    .map(Role::getRole)
                                    .map(RoleEnum::toString)
                                    .toList().contains(role))
                            .forEach(role -> roleRepository.saveAndFlush(new Role(RoleEnum.valueOf(role))));
        }
    }

    private void usersInit() {
        if (userRepository.count() != 0) return;

        userRepository.saveAllAndFlush(List.of(
                new User("roskonenov",
                        "roskonenov@gmail.com",
                        passwordEncoder.encode(initConfig.getPassword()))
                        .setRoles(List.of(roleRepository.findByRole(RoleEnum.ADMIN))),

                new User("elena_jelyazkova",
                        "elena_jelyazkova@gmail.com",
                        passwordEncoder.encode(initConfig.getPassword()))
                        .setRoles(List.of(roleRepository.findByRole(RoleEnum.USER))),

                new User("georgi79",
                        "georgi79@gmail.com",
                        passwordEncoder.encode(initConfig.getPassword()))
                        .setRoles(List.of(roleRepository.findByRole(RoleEnum.USER))))
        );
    }

    private void housingInit() throws IOException {
        if (housingRepository.count() != 0) return;

        Files.readAllLines(Path.of(HOUSING_INPUT_FILE_PATH))
                .forEach(line -> {
                    String[] fields = line.split("\\s+");
                    housingRepository.saveAndFlush(createHousingEntity(fields));
                });
    }

    private void carInit() throws IOException {
        if (carRepository.count() != 0) return;

        Files.readAllLines(Path.of(CAR_INPUT_FILE_PATH))
                .forEach(line -> {
                    String[] fields = line.split("\\s+");
                    carRepository.saveAndFlush(createCarEntity(fields));
                });
    }

    private Housing createHousingEntity(String[] fields) {
        return new Housing(
                HousingType.valueOf(fields[0]),
                cityRepository.findByName(getPropperString(fields[1])),
                getPropperString(fields[2]),
                BigDecimal.valueOf(Integer.parseInt(fields[3])),
                Integer.parseInt(fields[4]),
                Integer.parseInt(fields[5]),
                fields[6],
                userRepository.findByUsername(fields[7]).get());
    }

    private Car createCarEntity(String[] fields) {
        return new Car(cityRepository.findByName(getPropperString(fields[0])),
                getPropperString(fields[1]),
                BigDecimal.valueOf(Integer.parseInt(fields[2])),
                fields[3],
                userRepository.findByUsername(fields[4]).get(),
                fields[5],
                fields[6],
                Engine.valueOf(fields[7]),
                Integer.parseInt(fields[8]));
    }

    private static String getPropperString(String text) {
        return text.replaceAll("_", " ");
    }
}
