package RandevuApp.domain.user.dto;

import RandevuApp.commons.annotation.ValidPhone;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class PhoneChangeInitiateRequest {
    @NotBlank(message = "Password is required")
    private String password;

    @NotBlank(message = "New phone number is required")
    @ValidPhone
    private String newPhoneNumber;
}
