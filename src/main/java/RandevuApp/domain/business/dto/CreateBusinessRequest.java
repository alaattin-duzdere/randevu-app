package RandevuApp.domain.business.dto;

import RandevuApp.commons.validator.ValidTimeZone;
import RandevuApp.domain.business.model.Address;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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

    @NotNull(message = "Address details are required")
    @Valid
    private Address address;

    private String description;

    @ValidTimeZone
    private String timeZone;

    @Pattern(regexp = "^[a-z0-9-]+$", message = "Slug can only contain lowercase letters, numbers, and hyphens")
    @Size(max = 100)
    private String slug;
}
