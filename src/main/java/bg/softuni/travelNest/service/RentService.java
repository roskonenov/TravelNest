package bg.softuni.travelNest.service;

import bg.softuni.travelNest.model.dto.RentDTO;
import bg.softuni.travelNest.model.entity.rentEntity.HousingRentPeriod;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface RentService {

    List<HousingRentPeriod> getHousingRentPeriods(UUID housingId);

    boolean isAvailable(UUID houseId, LocalDate startDate, LocalDate endDate);

    String rent(RentDTO rentDTO);
}
