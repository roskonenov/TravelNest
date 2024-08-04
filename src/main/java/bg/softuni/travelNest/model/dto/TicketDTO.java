package bg.softuni.travelNest.model.dto;

import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
public class TicketDTO {

    private String username;

    @Positive(message = "{attraction.ticket.positive}")
    private int count;
}
