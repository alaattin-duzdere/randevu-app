package RandevuApp.commons.util;

import java.time.ZoneId;

public final class TimeZoneUtil {

    private TimeZoneUtil() {
    }

    public static boolean isValid(String value) {
        if (value == null || value.isBlank()) {
            return false;
        }
        return ZoneId.getAvailableZoneIds().contains(value);
    }

    public static ZoneId resolve(String configuredTimeZone, String fallbackTimeZone) {
        if (isValid(configuredTimeZone)) {
            return ZoneId.of(configuredTimeZone);
        }

        if (isValid(fallbackTimeZone)) {
            return ZoneId.of(fallbackTimeZone);
        }

        return ZoneId.systemDefault();
    }
}

