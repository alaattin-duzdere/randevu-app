package RandevuApp.domain.service_catalog.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record CreateServiceCatalogRequest(
        @NotBlank(message = "Catalog service name must not be blank") String name,
        String description,
        @NotNull(message = "Duration must be provided") @Min(value = 5, message = "Duration must be at least 5 minutes") Integer durationInMinutes,
        @NotNull(message = "Default price must be provided") @DecimalMin(value = "0.0", inclusive = true, message = "Price cannot be negative") BigDecimal price
) {}
