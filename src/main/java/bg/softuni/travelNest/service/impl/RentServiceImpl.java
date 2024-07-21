package bg.softuni.travelNest.service.impl;

import bg.softuni.travelNest.model.dto.RentDTO;
import bg.softuni.travelNest.model.entity.Car;
import bg.softuni.travelNest.model.entity.Housing;
import bg.softuni.travelNest.model.entity.rentPeriodEntity.CarRentPeriod;
import bg.softuni.travelNest.model.entity.rentPeriodEntity.HousingRentPeriod;
import bg.softuni.travelNest.repository.CarRepository;
import bg.softuni.travelNest.repository.RentRepository;
import bg.softuni.travelNest.repository.HousingRepository;
import bg.softuni.travelNest.service.RentService;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;

@Service
@AllArgsConstructor
public class RentServiceImpl implements RentService {

    private static final Logger LOGGER = LoggerFactory.getLogger(RentServiceImpl.class);

    private final RentRepository rentRepository;
    private final HousingRepository housingRepository;
    private final CarRepository carRepository;

    @Override
    public List<HousingRentPeriod> getHousingRentPeriods(UUID housingId) {
        return housingRepository.findById(housingId)
                .map(Housing::getRentPeriods)
                .orElse(new ArrayList<>());
    }

    @Override
    public List<CarRentPeriod> getCarRentPeriods(UUID propertyId) {
        return carRepository.findById(propertyId)
                .map(Car::getRentPeriods)
                .orElse(new ArrayList<>());
    }

    @Override
    public boolean isAvailable(UUID propertyId, LocalDate startDate, LocalDate endDate) {
        return getHousingRentPeriods(propertyId)
                .stream().noneMatch(period ->
                        (startDate.isBefore(period.getEndDate()) && endDate.isAfter(period.getStartDate())));
    }

    @Override
    public String rent(RentDTO rentDTO, String propertyType) {
        AtomicReference<String> message = new AtomicReference<>("Failed to rent the property!");

        if ("housing".equals(propertyType)) {
             housingRepository.findById(rentDTO.getId())
                    .ifPresent(housing -> {
                        if (isAvailable(rentDTO.getId(), rentDTO.getStartDate(), rentDTO.getEndDate())) {
                            message.set(applyRent(rentDTO, housing, propertyType));
                        } else {
                            message.set("The property is not available during the selected period!");
                        }
                    });
        }else {
            carRepository.findById(rentDTO.getId())
                    .ifPresent(car -> {
                        if (isAvailable(rentDTO.getId(), rentDTO.getStartDate(), rentDTO.getEndDate())) {
                            message.set(applyRent(rentDTO, car, propertyType));
                        } else {
                            message.set("The property is not available during the selected period!");
                        }
                    });
        }
        return message.get();
    }

    private String applyRent(RentDTO rentDTO, Object object, String propertyType) {

        if ("housing".equals(propertyType)) {

            rentRepository.saveAndFlush(new HousingRentPeriod(
                    rentDTO.getRenter().getId(),
                    rentDTO.getStartDate(),
                    rentDTO.getEndDate(),
                    (Housing) object));

            return "The housing was rented successfully!";
        } else {
            rentRepository.saveAndFlush(new CarRentPeriod(
                    rentDTO.getRenter().getId(),
                    rentDTO.getStartDate(),
                    rentDTO.getEndDate(),
                    (Car) object));

            return "The car was rented successfully!";
        }
    }

    @Scheduled(cron = "*/15 0-2 14 * * *")
    private void cleanRentDatabase(){
        rentRepository.deleteAll(rentRepository.findByEndDateIsLessThanEqual(LocalDate.now())
                .orElse(new ArrayList<>()));

        LOGGER.info("Database cleaned");
    }
}
