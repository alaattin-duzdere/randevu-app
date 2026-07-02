package RandevuApp.commons.validator;

import RandevuApp.commons.annotation.TimeZoneValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = TimeZoneValidator.class)
public @interface ValidTimeZone {
    String message() default "Invalid time zone";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
