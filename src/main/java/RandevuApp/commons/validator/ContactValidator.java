package RandevuApp.commons.validator;

import org.springframework.stereotype.Component;

import java.util.regex.Pattern;

@Component
public class ContactValidator {

    private static final Pattern EMAIL_PATTERN = Pattern.compile(
            "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$"
    );
    private static final Pattern PHONE_PATTERN = Pattern.compile(
            "^\\+?[0-9. ()-]{7,25}$"
    );

    public boolean isValidEmail(String email) {
        if (email == null) return false;
        return EMAIL_PATTERN.matcher(email).matches();
    }

    public boolean isValidPhoneNumber(String phone) {
        if (phone == null) return false;
        return PHONE_PATTERN.matcher(phone).matches();
    }
}
