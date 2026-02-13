package RandevuApp.domain.business.dto;

import RandevuApp.commons.validator.ValidTimeZone;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateBusinessRequest {
    @NotBlank(message = "Business name cannot be empty")
    private String name;

    @NotBlank(message = "Address cannot be empty")
    private String address;

    private String description;

    @ValidTimeZone
    private String timeZone;

    private Boolean active = true; // this field is optional, maybe you can create another endpoint for this field
}
