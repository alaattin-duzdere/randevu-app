package RandevuApp.domain.service_offering.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;

import java.math.BigDecimal;

public record UpdateServiceOfferingRequest(
        String name,
        String description,
        @Min(value = 5, message = "Service duration must be at least 5 minutes") Integer durationInMinutes,
        @DecimalMin(value = "0.0", inclusive = true, message = "Price cannot be negative") BigDecimal price,
        Boolean active
) {}
