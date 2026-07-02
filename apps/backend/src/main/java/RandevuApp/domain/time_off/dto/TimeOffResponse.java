package RandevuApp.domain.time_off.dto;

import RandevuApp.domain.time_off.model.TimeOffType;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record TimeOffResponse(
        Long id,
        Long staffId,
        LocalDateTime startTime,
        LocalDateTime endTime,
        TimeOffType type,
        String note
) {}
