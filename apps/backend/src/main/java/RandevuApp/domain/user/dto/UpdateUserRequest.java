package RandevuApp.domain.user.dto;

import RandevuApp.domain.user.model.Gender;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record UpdateUserRequest(
        @Size(min = 2, max = 50, message = "Name must be between 2 and 50 characters")
        @Pattern(regexp = "^[\\p{L} .'-]+$", message = "Name contains invalid characters") String firstName,
        @Size(min = 2, max = 50, message = "Name must be between 2 and 50 characters")
        @Pattern(regexp = "^[\\p{L} .'-]+$", message = "Name contains invalid characters") String lastName,
        Gender gender
) {}
