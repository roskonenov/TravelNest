package bg.softuni.travelNest.model.dto;

import bg.softuni.travelNest.model.entity.User;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
public class RentDTO {

    @NotBlank
    @FutureOrPresent
    private LocalDate startDate;

    @NotBlank
    @FutureOrPresent
    private LocalDate endDate;

    @NotNull
    private UUID id;

    @NotNull
    private User renter;
}
