package RandevuApp.domain.time_off.dto;

import RandevuApp.domain.time_off.model.TimeOffType;

import java.time.LocalDateTime;

public record UpdateTimeOffRequest(
        LocalDateTime startTime,
        LocalDateTime endTime,
        TimeOffType type,
        String note
) {}
