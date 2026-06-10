package RandevuApp.commons.util;

public final class DataNormalizationUtil {

    private DataNormalizationUtil() {
        // Utility class
    }

    public static String normalizeEmail(String email) {
        if (email == null) return null;
        return email.toLowerCase().trim();
    }

    public static String normalizePhone(String phone) {
        if (phone == null) return null;
        // Keeps only the '+' sign and digits. Removes spaces, dashes, parentheses.
        String cleaned = phone.replaceAll("[^+\\d]", "");

        // Ensure it starts with '+' for E.164 compliance, in case the
        // frontend only sent digits without the prefix.
        if (!cleaned.isEmpty() && !cleaned.startsWith("+")) {
            cleaned = "+" + cleaned;
        }

        return cleaned;
    }

    public static String normalizeName(String name) {
        if (name == null) return null;
        return name.trim();
    }
}