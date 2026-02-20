package RandevuApp.domain.service_offering.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class UpdateServiceOfferingRequest {

    private String name;

    private String description;

    @Min(value = 5, message = "Hizmet süresi en az 5 dakika olmalıdır")
    private Integer durationInMinutes;

    @DecimalMin(value = "0.0", inclusive = true, message = "Fiyat 0'dan küçük olamaz")
    private BigDecimal price;

    private Boolean active;
}
