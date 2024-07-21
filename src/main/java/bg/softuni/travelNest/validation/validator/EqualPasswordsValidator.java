package bg.softuni.travelNest.validation.validator;

import bg.softuni.travelNest.model.dto.RegisterDto;
import bg.softuni.travelNest.validation.annotation.EqualPasswords;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.hibernate.validator.constraintvalidation.HibernateConstraintValidatorContext;

public class EqualPasswordsValidator implements ConstraintValidator<EqualPasswords, RegisterDto> {
    private String message;

    @Override
    public void initialize(EqualPasswords constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
        this.message = constraintAnnotation.message();
    }

    @Override
    public boolean isValid(RegisterDto dto, ConstraintValidatorContext context) {

        boolean isEqual = dto.getPassword().equals(dto.getConfirmPassword());

        if (!isEqual) {
            context
                    .unwrap(HibernateConstraintValidatorContext.class)
                    .buildConstraintViolationWithTemplate(message)
                    .addPropertyNode("confirmPassword")
                    .addConstraintViolation()
                    .disableDefaultConstraintViolation();
        }
        return isEqual;
    }
}
