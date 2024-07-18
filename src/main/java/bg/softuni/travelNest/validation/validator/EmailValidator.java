package bg.softuni.travelNest.validation.validator;

import bg.softuni.travelNest.validation.annotation.Email;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EmailValidator implements ConstraintValidator<Email, String> {

    @Override
    public void initialize(Email email) {
        ConstraintValidator.super.initialize(email);
    }

    @Override
    public boolean isValid(String email, ConstraintValidatorContext context) {

        String regex = "^(\\w+([.\\-_]*?\\w)*?)@(\\w+)(\\.\\w+)+$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }
}
