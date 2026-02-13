package RandevuApp.domain.business.dto;

import RandevuApp.commons.validator.ValidTimeZone;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateBusinessRequest {

    @NotBlank(message = "Business name cannot be blank")
    @Size(min = 2, max = 100)
    private String name;

    @NotBlank(message = "Address cannot be blank")
    private String address;

    private String description;

    @ValidTimeZone
    private String timeZone;

    @Pattern(regexp = "^[a-z0-9-]+$", message = "Slug can only contain lowercase letters, numbers, and hyphens")
    @Size(max = 100)
    private String slug;
}
