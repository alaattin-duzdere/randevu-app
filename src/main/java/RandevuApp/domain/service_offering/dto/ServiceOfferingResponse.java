package RandevuApp.domain.service_offering.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.Instant;

@Data
@Builder
public class ServiceOfferingResponse {
    private Long id;
    private Long businessId;
    private String name;
    private String description;
    private Integer durationInMinutes;
    private BigDecimal price;
    private boolean active;
}
