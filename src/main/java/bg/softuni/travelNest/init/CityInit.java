package bg.softuni.travelNest.init;

import bg.softuni.travelNest.model.entity.CityEntity;
import bg.softuni.travelNest.model.enums.City;
import bg.softuni.travelNest.repository.CityRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
public class CityInit implements CommandLineRunner {
    private final CityRepository cityRepository;

    public CityInit(CityRepository cityRepository) {
        this.cityRepository = cityRepository;
    }

    @Override
    public void run(String... args) throws Exception {
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
}
