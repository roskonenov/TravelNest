package bg.softuni.travelNest.service.impl;

import bg.softuni.travelNest.config.Messages;
import bg.softuni.travelNest.exception.ObjectNotFoundException;
import bg.softuni.travelNest.model.dto.RentDTO;
import bg.softuni.travelNest.model.entity.Car;
import bg.softuni.travelNest.model.entity.Housing;
import bg.softuni.travelNest.model.entity.rentPeriodEntity.CarRentPeriod;
import bg.softuni.travelNest.model.entity.rentPeriodEntity.HousingRentPeriod;
import bg.softuni.travelNest.repository.CarRepository;
import bg.softuni.travelNest.repository.RentRepository;
import bg.softuni.travelNest.repository.HousingRepository;
import bg.softuni.travelNest.service.RentService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;

@Service
@RequiredArgsConstructor
public class RentServiceImpl implements RentService {

    private static final Logger LOGGER = LoggerFactory.getLogger(RentServiceImpl.class);

    private final RentRepository rentRepository;
    private final HousingRepository housingRepository;
    private final CarRepository carRepository;
    private final Messages messages;

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
    public boolean isAvailable(String entityType, UUID propertyId, LocalDate startDate, LocalDate endDate) {
        if ("housing".equals(entityType)) {
            return getHousingRentPeriods(propertyId)
                    .stream().noneMatch(period ->
                            (startDate.isBefore(period.getEndDate()) && endDate.isAfter(period.getStartDate())));
        } else if ("car".equals(entityType)){
            return getCarRentPeriods(propertyId)
                    .stream().noneMatch(period ->
                            (startDate.isBefore(period.getEndDate()) && endDate.isAfter(period.getStartDate())));
        }
        throw new ObjectNotFoundException(messages.get("message.error.wrong"));
    }

    @Override
    public String rent(RentDTO rentDTO, String propertyType) {
        AtomicReference<String> message = new AtomicReference<>(messages.get("message.error.failed"));

        if ("housing".equals(propertyType)) {
             housingRepository.findById(rentDTO.getId())
                    .ifPresent(housing -> {
                        if (isAvailable(propertyType, rentDTO.getId(), rentDTO.getStartDate(), rentDTO.getEndDate())) {
                            message.set(applyRent(rentDTO, housing, propertyType));
                        } else {
                            message.set(messages.get("message.housing.not.available"));
                        }
                    });
        }else if ("car".equals(propertyType)){
            carRepository.findById(rentDTO.getId())
                    .ifPresent(car -> {
                        if (isAvailable(propertyType, rentDTO.getId(), rentDTO.getStartDate(), rentDTO.getEndDate())) {
                            message.set(applyRent(rentDTO, car, propertyType));
                        } else {
                            message.set(messages.get("message.car.not.available"));
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

            return messages.get("message.housing.rented");
        } else {
            rentRepository.saveAndFlush(new CarRentPeriod(
                    rentDTO.getRenter().getId(),
                    rentDTO.getStartDate(),
                    rentDTO.getEndDate(),
                    (Car) object));

            return messages.get("message.car.rented");
        }
    }

    @Scheduled(cron = "0 0 14 * * *")
    private void cleanRentDatabase(){
        rentRepository.deleteAll(rentRepository.findByEndDateIsLessThanEqual(LocalDate.now())
                .orElse(new ArrayList<>()));

        LOGGER.info("Database cleaned");
    }
}
