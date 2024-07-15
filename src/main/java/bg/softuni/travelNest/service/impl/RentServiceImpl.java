package bg.softuni.travelNest.service.impl;

import bg.softuni.travelNest.model.dto.RentDTO;
import bg.softuni.travelNest.model.entity.Housing;
import bg.softuni.travelNest.model.entity.rentEntity.HousingRentPeriod;
import bg.softuni.travelNest.repository.HousingRentRepository;
import bg.softuni.travelNest.repository.HousingRepository;
import bg.softuni.travelNest.service.RentService;
import lombok.AllArgsConstructor;
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

    private final HousingRentRepository housingRentRepository;
    private final HousingRepository housingRepository;

    @Override
    public List<HousingRentPeriod> getHousingRentPeriods(UUID housingId) {
        return housingRepository.findById(housingId)
                .map(Housing::getRentPeriods)
                .orElse(new ArrayList<>());
    }

    @Override
    public boolean isAvailable(UUID housingId, LocalDate startDate, LocalDate endDate) {
        return getHousingRentPeriods(housingId)
                .stream().noneMatch(period ->
                        (startDate.isBefore(period.getEndDate()) && endDate.isAfter(period.getStartDate())));
    }

    @Override
    public String rent(RentDTO rentDTO) {
        AtomicReference<String> message = new AtomicReference<>("Failed to rent the housing!");

        Optional<Housing> optionalHousing = housingRepository.findById(rentDTO.getId());

        optionalHousing
                .ifPresent(housing -> {
                    if (isAvailable(rentDTO.getId(), rentDTO.getStartDate(), rentDTO.getEndDate())) {
                        message.set(applyRent(rentDTO, housing));
                    } else {
                        message.set("The housing is not available during the selected period!");
                    }
                });
        return message.get();
    }

    private String applyRent(RentDTO rentDTO, Housing housing) {
        housing.setAvailable(false);
        housingRentRepository.saveAndFlush(new HousingRentPeriod(rentDTO.getRenter().getId(), rentDTO.getStartDate(), rentDTO.getEndDate(), housing ));
        housingRepository.saveAndFlush(housing);
        return "The housing was rented successfully!";
    }
}
