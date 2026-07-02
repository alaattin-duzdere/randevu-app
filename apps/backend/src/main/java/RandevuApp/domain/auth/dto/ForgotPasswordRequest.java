package RandevuApp.domain.auth.dto;

import RandevuApp.commons.annotation.ValidPhone;
import RandevuApp.commons.util.DataNormalizationUtil;
import jakarta.validation.constraints.NotBlank;

public record ForgotPasswordRequest(
        @NotBlank(message = "Phone number cannot be blank")
        @ValidPhone
        String phoneNumber
) {
    // Compact constructor for automatic data normalization
    public ForgotPasswordRequest {
        phoneNumber = DataNormalizationUtil.normalizePhone(phoneNumber);
    }
}
