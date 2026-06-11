package RandevuApp.domain.business.dto;

import lombok.Builder;

import java.time.LocalDate;
import java.time.LocalTime;

@Builder
public record ScheduleOverrideResponse(
        Long id,
        LocalDate targetDate,
        Boolean isClosed,
        LocalTime openTime,
        LocalTime closeTime,
        String reason
) {}
