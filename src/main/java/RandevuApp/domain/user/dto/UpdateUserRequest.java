package RandevuApp.domain.user.dto;

import RandevuApp.domain.user.model.Gender;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UpdateUserRequest {

    @Size(min = 2, max = 50, message = "Name must be between 2 and 50 characters")
    @Pattern(regexp = "^[\\p{L} .'-]+$", message = "Name contains invalid characters")
    private String firstName;

    @Size(min = 2, max = 50, message = "Name must be between 2 and 50 characters")
    @Pattern(regexp = "^[\\p{L} .'-]+$", message = "Name contains invalid characters")
    private String lastName;

    private Gender gender;

    @Size(max = 255)
    private String address;
}
