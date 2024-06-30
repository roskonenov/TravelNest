package bg.softuni.travelNest.data.dto;

import bg.softuni.travelNest.data.annotation.Email;
import bg.softuni.travelNest.data.annotation.Password;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

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

    public String getUsername() {
        return username;
    }

    public RegisterDto setUsername(String username) {
        this.username = username;
        return this;
    }

    public String getEmail() {
        return email;
    }

    public RegisterDto setEmail(String email) {
        this.email = email;
        return this;
    }

    public String getPassword() {
        return password;
    }

    public RegisterDto setPassword(String password) {
        this.password = password;
        return this;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public RegisterDto setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
        return this;
    }

    public boolean isAgree() {
        return agree;
    }

    public RegisterDto setAgree(boolean agree) {
        this.agree = agree;
        return this;
    }
}
