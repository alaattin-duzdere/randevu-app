package RandevuApp.domain.auth.dto;

import RandevuApp.commons.annotation.ValidPhone;
import RandevuApp.commons.util.ContactFormatUtil;
import RandevuApp.commons.util.DataNormalizationUtil;
import RandevuApp.domain.user.model.Gender;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record RegisterRequest(
        @NotBlank(message = "Email cannot be blank")
        @Email(message = "Please provide a valid email address", regexp = ContactFormatUtil.EMAIL_REGEX)
        String email,

        @NotBlank(message = "Phone number cannot be blank")
        @ValidPhone
        String phoneNumber,

        @NotBlank(message = "Password cannot be blank")
        @Size(min = 8, message = "Password must be at least 8 characters long")
        String password,

        @NotBlank(message = "First name cannot be blank")
        @Size(min = 2, max = 50, message = "Name must be between 2 and 50 characters")
        @Pattern(regexp = "^[\\p{L} .'-]+$", message = "Name contains invalid characters")
        String firstName,

        @NotBlank(message = "Last name cannot be blank")
        @Size(min = 2, max = 50, message = "Name must be between 2 and 50 characters")
        @Pattern(regexp = "^[\\p{L} .'-]+$", message = "Name contains invalid characters")
        String lastName,

        Gender gender
) {
    // Compact constructor for automatic data normalization
    public RegisterRequest {
        email = DataNormalizationUtil.normalizeEmail(email);
        phoneNumber = DataNormalizationUtil.normalizePhone(phoneNumber);
        firstName = DataNormalizationUtil.normalizeName(firstName);
        lastName = DataNormalizationUtil.normalizeName(lastName);
    }
}
