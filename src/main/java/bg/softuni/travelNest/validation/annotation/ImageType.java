package bg.softuni.travelNest.validation.annotation;

import bg.softuni.travelNest.validation.validator.ImageTypeValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@Constraint(validatedBy = ImageTypeValidator.class)
public @interface ImageType {

    String message() default "Invalid file";

    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
