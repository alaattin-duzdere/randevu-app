package RandevuApp.domain.user.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ContactChangeInitiateRequest {
    @NotBlank
    private String password;

    @NotBlank
    private String newValue;
}
