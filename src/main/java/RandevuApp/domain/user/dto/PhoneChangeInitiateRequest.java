package RandevuApp.domain.user.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class PhoneChangeInitiateRequest {
    @NotBlank(message = "Password is required")
    private String password;

    @NotBlank(message = "New phone number is required")
    @Pattern(regexp = "^\\+?[0-9. ()-]{7,25}$", message = "Invalid phone number format")
    private String newPhoneNumber;
}
