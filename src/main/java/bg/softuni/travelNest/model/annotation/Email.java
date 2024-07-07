package bg.softuni.travelNest.model.annotation;

import jakarta.validation.Constraint;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@Constraint(validatedBy = Email.EmailValidator.class)
public @interface Email {

    String message() default "Invalid email";

    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

    class EmailValidator implements ConstraintValidator<Email, String> {

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
}
