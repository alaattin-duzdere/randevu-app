package RandevuApp.domain.service_catalog.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class ServiceCatalogResponse {
    private Long id;
    private String name;
    private String description;
    private Integer durationInMinutes;
    private BigDecimal price;
    private boolean active;
}