package RandevuApp.domain.auth.dto;

import RandevuApp.commons.util.DataNormalizationUtil;
import jakarta.validation.constraints.NotBlank;

public record ResendVerificationRequest(
        @NotBlank(message = "Identifier cannot be blank")
        String identifier
) {
    // Compact constructor for automatic data normalization
    public ResendVerificationRequest {
        if (identifier != null) {
            identifier = identifier.contains("@") ?
                    DataNormalizationUtil.normalizeEmail(identifier) :
                    DataNormalizationUtil.normalizePhone(identifier);
        }
    }
}
