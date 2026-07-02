package RandevuApp.domain.user.dto;

import RandevuApp.commons.annotation.ValidPhone;
import jakarta.validation.constraints.NotBlank;

public record PhoneChangeInitiateRequest(
        @NotBlank(message = "Password is required") String password,
        @NotBlank(message = "New phone number is required") @ValidPhone String newPhoneNumber
) {}
