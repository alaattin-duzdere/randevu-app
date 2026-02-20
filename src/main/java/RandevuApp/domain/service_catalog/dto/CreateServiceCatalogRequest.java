package RandevuApp.domain.service_catalog.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.math.BigDecimal;

@Data
public class CreateServiceCatalogRequest {
    @NotBlank(message = "Katalog hizmet adı boş olamaz")
    private String name;

    private String description;

    @NotNull(message = "Süre belirtilmelidir")
    @Min(value = 5, message = "Süre en az 5 dakika olmalıdır")
    private Integer durationInMinutes;

    @NotNull(message = "Varsayılan fiyat belirtilmelidir")
    @DecimalMin(value = "0.0", inclusive = true, message = "Fiyat 0'dan küçük olamaz")
    private BigDecimal price;
}
