package RandevuApp.domain.service_catalog.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class UpdateServiceCatalogRequest {

    private String name;

    private String description;

    @Min(value = 5, message = "Süre en az 5 dakika olmalıdır")
    private Integer durationInMinutes;

    @DecimalMin(value = "0.0", inclusive = true, message = "Fiyat 0'dan küçük olamaz")
    private BigDecimal price;

    private Boolean active;
}
