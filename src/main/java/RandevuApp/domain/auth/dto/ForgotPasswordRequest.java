package RandevuApp.domain.auth.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Data
public class ForgotPasswordRequest {

    @NotBlank(message = "Email veya Telefon numarası boş olamaz")
    private String recipient;
}
