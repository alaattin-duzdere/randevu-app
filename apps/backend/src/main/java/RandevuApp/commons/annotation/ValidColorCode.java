package RandevuApp.commons.annotation;

import RandevuApp.commons.validator.ColorCodeValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = ColorCodeValidator.class)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidColorCode {
    String message() default "Invalid Color Format. Example: #FF5733";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}