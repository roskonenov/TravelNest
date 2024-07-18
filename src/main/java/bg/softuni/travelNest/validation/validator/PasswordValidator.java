package bg.softuni.travelNest.validation.validator;

import bg.softuni.travelNest.validation.annotation.Password;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PasswordValidator implements ConstraintValidator<Password, String> {
    private static final int MIN_LENGTH = 3;
    private static final int MAX_LENGTH = 20;

    @Override
    public void initialize(Password password) {
        ConstraintValidator.super.initialize(password);
    }

    @Override
    public boolean isValid(String password, ConstraintValidatorContext context) {

        if (password.length() < MIN_LENGTH || password.length() > MAX_LENGTH){
            return false;
        }

        String regex = "^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[!@#$%^&*()_+<>?]).*?$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(password);
        return matcher.matches();
    }
}
