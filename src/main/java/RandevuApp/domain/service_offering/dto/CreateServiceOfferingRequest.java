package RandevuApp.domain.service_offering.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class CreateServiceOfferingRequest {

    @NotBlank(message = "Hizmet adı boş olamaz")
    private String name;

    private String description;

    @NotNull(message = "Hizmet süresi belirtilmelidir")
    @Min(value = 5, message = "Hizmet süresi en az 5 dakika olmalıdır")
    private Integer durationInMinutes;

    @NotNull(message = "Hizmet fiyatı belirtilmelidir")
    @DecimalMin(value = "0.0", inclusive = true, message = "Fiyat 0'dan küçük olamaz")
    private BigDecimal price;
}
