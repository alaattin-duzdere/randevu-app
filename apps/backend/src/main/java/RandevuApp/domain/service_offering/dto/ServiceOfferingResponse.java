package RandevuApp.domain.service_offering.dto;

import lombok.Builder;

import java.math.BigDecimal;

@Builder
public record ServiceOfferingResponse(
        Long id,
        Long businessId,
        String name,
        String description,
        Integer durationInMinutes,
        BigDecimal price,
        boolean active
) {}
