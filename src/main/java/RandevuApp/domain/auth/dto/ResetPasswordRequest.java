package RandevuApp.domain.auth.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

public record ResetPasswordRequest(
        @NotEmpty
        String resetToken,

        @NotEmpty
        @Size(min = 8, max = 64, message = "Password must be between 8 and 64 characters")
        String newPassword,

        @NotEmpty
        @Size(min = 8, max = 64, message = "Password must be between 8 and 64 characters")
        String confirmNewPassword
) {}
