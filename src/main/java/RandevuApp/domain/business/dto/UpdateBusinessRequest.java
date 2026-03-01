package RandevuApp.domain.business.dto;

import RandevuApp.commons.validator.ValidTimeZone;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateBusinessRequest {
    @NotBlank(message = "Business name cannot be empty")
    private String name;

    @NotNull(message = "Address details are required")
    @Valid
    private AddressDto address;

    private String description;

    @ValidTimeZone
    private String timeZone;

    private Boolean active = true; // this field is optional, maybe you can create another endpoint for this field
}
