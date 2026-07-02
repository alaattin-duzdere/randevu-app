package RandevuApp.domain.service_offering.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record CreateServiceOfferingRequest(
        @NotBlank(message = "Service name must not be blank") String name,
        String description,
        @NotNull(message = "Service duration must be provided") @Min(value = 5, message = "Service duration must be at least 5 minutes") Integer durationInMinutes,
        @NotNull(message = "Service price must be provided") @DecimalMin(value = "0.0", inclusive = true, message = "Price cannot be negative") BigDecimal price
) {}
