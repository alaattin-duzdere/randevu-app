package RandevuApp.domain.auth.dto;

import RandevuApp.commons.annotation.ValidPhone;
import RandevuApp.commons.util.DataNormalizationUtil;
import jakarta.validation.constraints.NotBlank;

public record VerifyOtpRequest(
        @NotBlank
        @ValidPhone
        String phoneNumber,

        @NotBlank
        String otpCode
) {
    // Compact constructor for automatic data normalization
    public VerifyOtpRequest {
        phoneNumber = DataNormalizationUtil.normalizePhone(phoneNumber);
        if (otpCode != null) {
            otpCode = otpCode.trim();
        }
    }
}
