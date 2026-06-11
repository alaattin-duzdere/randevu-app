package RandevuApp.domain.business.dto;

import java.time.LocalTime;

public record BusinessSettingsResponse(
        Integer slotDurationTime,
        LocalTime openingTime,
        LocalTime closingTime,
        Long businessId
) {}
