package bg.softuni.travelNest.validation.annotation;

import bg.softuni.travelNest.validation.validator.PasswordValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@Constraint(validatedBy = PasswordValidator.class)
public @interface Password {
    String message() default "Invalid password";

    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
