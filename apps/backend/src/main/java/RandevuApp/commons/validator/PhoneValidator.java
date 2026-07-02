package RandevuApp.commons.validator;

import RandevuApp.commons.annotation.ValidPhone;
import RandevuApp.commons.util.ContactFormatUtil;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PhoneValidator implements ConstraintValidator<ValidPhone, String> {

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null || value.isBlank()) {
            return true;
        }
        return ContactFormatUtil.isPhone(value);
    }
}