package bg.softuni.travelNest.validation.annotation;

import bg.softuni.travelNest.validation.validator.EqualPasswordsValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Constraint(validatedBy = EqualPasswordsValidator.class)
public @interface EqualPasswords {

    String message() default "Passwords did not match!";

    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
