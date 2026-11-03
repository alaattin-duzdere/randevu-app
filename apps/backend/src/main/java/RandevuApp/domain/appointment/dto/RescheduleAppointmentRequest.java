package RandevuApp.domain.appointment.dto;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record RescheduleAppointmentRequest(
        @NotNull(message = "New start time is required")
        @Future(message = "New start time must be in the future")
        LocalDateTime newStartTime,
        @NotNull(message = "New end time is required")
        @Future(message = "New end time must be in the future")
        LocalDateTime newEndTime
) {
}
