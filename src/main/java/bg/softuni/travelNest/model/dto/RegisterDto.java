package bg.softuni.travelNest.model.dto;

import bg.softuni.travelNest.validation.annotation.Email;
import bg.softuni.travelNest.validation.annotation.Password;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class RegisterDto {

    @NotBlank(message = "{user.username.not.empty}")
    @Size(min = 3, max = 20, message = "{user.username.length}")
    private String username;

    @NotBlank(message = "{user.email.not.empty}")
    @Email(message = "{user.email.invalid}")
    private String email;

    @NotBlank(message = "{user.password.not.empty}")
    @Password(message = "{user.password.invalid}")
    @Size(min = 3, max = 20, message = "{user.password.length}")
    private String password;

    @NotBlank(message = "{user.password.not.empty}")
    @Password(message = "{user.password.invalid}")
    @Size(min = 3, max = 20, message = "{user.password.length}")
    private String confirmPassword;

    @AssertTrue(message = "{user.terms.false}")
    private boolean agree;
}
