package bg.softuni.travelNest.service;

import bg.softuni.travelNest.model.dto.RentDTO;
import bg.softuni.travelNest.model.entity.rentPeriodEntity.CarRentPeriod;
import bg.softuni.travelNest.model.entity.rentPeriodEntity.HousingRentPeriod;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface RentService {

    List<HousingRentPeriod> getHousingRentPeriods(UUID housingId);

    List<CarRentPeriod> getCarRentPeriods(UUID propertyId);

    boolean isAvailable(String propertyType, UUID houseId, LocalDate startDate, LocalDate endDate);

    String rent(RentDTO rentDTO, String propertyType);
}
