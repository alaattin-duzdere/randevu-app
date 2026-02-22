package RandevuApp.commons.annotation;

import RandevuApp.commons.validator.PhoneValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = PhoneValidator.class)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidPhone {

    String message() default "Geçersiz telefon numarası formatı. Geçerli karakterler: rakamlar, boşluk, +, -, (, )";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}