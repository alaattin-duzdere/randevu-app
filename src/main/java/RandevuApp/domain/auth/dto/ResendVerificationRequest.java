package RandevuApp.domain.auth.dto;

import RandevuApp.commons.annotation.ValidPhone;
import RandevuApp.commons.util.DataNormalizationUtil;
import jakarta.validation.constraints.NotBlank;

public record ResendVerificationRequest(
        @NotBlank(message = "Phone number cannot be blank")
        @ValidPhone(message = "Invalid phone number format")
        String phoneNumber
) {
    // Compact constructor for automatic data normalization
    public ResendVerificationRequest {
        if (phoneNumber != null) {
            phoneNumber = DataNormalizationUtil.normalizePhone(phoneNumber);
        }
    }
}
