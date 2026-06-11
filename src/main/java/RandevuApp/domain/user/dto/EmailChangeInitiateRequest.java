package RandevuApp.domain.user.dto;

import RandevuApp.commons.util.ContactFormatUtil;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record EmailChangeInitiateRequest(
        @NotBlank(message = "Password is required") String password,
        @NotBlank(message = "New email is required")
        @Email(message = "Invalid email format",regexp = ContactFormatUtil.EMAIL_REGEX)
        String newEmail
) {}
