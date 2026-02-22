package RandevuApp.commons.util;

import java.util.regex.Pattern;

public final class ContactFormatUtil {

    private static final Pattern EMAIL_PATTERN = Pattern.compile(
            "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$"
    );
    private static final Pattern PHONE_PATTERN = Pattern.compile(
            "^\\+?[0-9. ()-]{7,25}$"
    );

    private ContactFormatUtil() { }

    public static boolean isEmail(String value) {
        if (value == null || value.isBlank()) return false;
        return EMAIL_PATTERN.matcher(value).matches();
    }

    public static boolean isPhone(String value) {
        if (value == null || value.isBlank()) return false;
        return PHONE_PATTERN.matcher(value).matches();
    }
}