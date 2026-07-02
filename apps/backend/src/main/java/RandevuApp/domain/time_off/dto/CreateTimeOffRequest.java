package RandevuApp.domain.time_off.dto;

import RandevuApp.domain.time_off.model.TimeOffType;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record CreateTimeOffRequest(
        @NotNull(message = "Start time must not be null")
        @FutureOrPresent(message = "Cannot create time off in the past") LocalDateTime startTime,

        @NotNull(message = "End time must not be null")
        @FutureOrPresent(message = "Cannot create time off in the past") LocalDateTime endTime,

        @NotNull(message = "Time off type must be provided") TimeOffType type,

        String note
) {}
