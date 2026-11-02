package RandevuApp.commons.annotation;

import RandevuApp.commons.util.TimeZoneUtil;
import RandevuApp.commons.validator.ValidTimeZone;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class TimeZoneValidator implements ConstraintValidator<ValidTimeZone, String> {
    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return TimeZoneUtil.isValid(value);
    }
}
