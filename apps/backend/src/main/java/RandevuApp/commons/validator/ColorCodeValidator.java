package RandevuApp.commons.validator;

import RandevuApp.commons.annotation.ValidColorCode;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.regex.Pattern;

public class ColorCodeValidator implements ConstraintValidator<ValidColorCode, String> {

    private static final Pattern COLOR_PATTERN = Pattern.compile("^#([A-Fa-f0-9]{6}|[A-Fa-f0-9]{3})$");

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null || value.isBlank()) {
            return true;
        }
        return COLOR_PATTERN.matcher(value).matches();
    }
}
