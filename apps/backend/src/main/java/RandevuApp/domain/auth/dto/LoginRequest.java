package RandevuApp.domain.auth.dto;

import RandevuApp.commons.util.DataNormalizationUtil;
import jakarta.validation.constraints.NotBlank;

public record LoginRequest(
        @NotBlank(message = "Identifier cannot be blank")
        String identifier,

        @NotBlank(message = "Password cannot be blank")
        String password,

        Boolean rememberMe
) {
    // Compact constructor for automatic data normalization
    public LoginRequest {
        if (identifier != null) {
            identifier = identifier.contains("@") ?
                    DataNormalizationUtil.normalizeEmail(identifier) :
                    DataNormalizationUtil.normalizePhone(identifier);
        }

        if (rememberMe == null) {
            rememberMe = false;
        }
    }
}
