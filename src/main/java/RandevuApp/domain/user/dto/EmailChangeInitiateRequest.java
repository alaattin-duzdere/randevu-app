package RandevuApp.domain.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class EmailChangeInitiateRequest {
    @NotBlank(message = "Password is required")
    private String password;

    @NotBlank(message = "New email is required")
    @Email(message = "Invalid email format")
    private String newEmail;
}
