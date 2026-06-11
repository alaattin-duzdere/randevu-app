package RandevuApp.domain.business.dto;

import RandevuApp.commons.validator.ValidTimeZone;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record UpdateBusinessRequest(
        @NotBlank(message = "Business name cannot be empty") String name,

        @NotNull(message = "Address details are required") @Valid AddressDto address,

        String description,

        @ValidTimeZone String timeZone,

        Boolean active
) {
    public UpdateBusinessRequest {
        if (active == null) active = true;
    }
}
