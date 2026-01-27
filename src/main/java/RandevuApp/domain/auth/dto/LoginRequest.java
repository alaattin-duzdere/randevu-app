package RandevuApp.domain.auth.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class LoginRequest {

    @NotBlank(message = "identifier cannot be blank")
    private String identifier;

    @NotBlank(message = "Password cannot be blank")
    private String password;
}
