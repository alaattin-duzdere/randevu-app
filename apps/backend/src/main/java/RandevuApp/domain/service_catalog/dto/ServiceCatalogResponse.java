package RandevuApp.domain.service_catalog.dto;

import lombok.Builder;

import java.math.BigDecimal;

@Builder
public record ServiceCatalogResponse(
        Long id,
        String name,
        String description,
        Integer durationInMinutes,
        BigDecimal price,
        boolean active
) {}
