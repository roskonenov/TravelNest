package bg.softuni.travelNest.data.annotation;

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
@Constraint(validatedBy = Password.PasswordValidator.class)
public @interface Password {
    String message() default "Invalid password";

    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

    class PasswordValidator implements ConstraintValidator<Password, String>{
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

}
