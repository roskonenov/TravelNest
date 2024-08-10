package bg.softuni.travelNest.model.dto;

import bg.softuni.travelNest.validation.annotation.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class EmailMessageDTO {

    @NotBlank(message = "{user.name.not.empty}")
    private String name;
    @PositiveOrZero(message = "{user.number.positive}")
    private Integer number;
    @Email(message = "{user.email.invalid}")
    private String email;
    @NotBlank(message = "{message.not.empty}")
    private String text;
}
