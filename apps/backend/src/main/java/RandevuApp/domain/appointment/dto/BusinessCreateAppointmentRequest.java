package RandevuApp.domain.appointment.dto;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record BusinessCreateAppointmentRequest(
        @NotNull(message = "User ID is required")
        Long userId,

        @NotNull(message = "Staff ID is required")
        Long staffId,

        @NotNull(message = "Service ID is required")
        Long serviceId,

        @NotNull(message = "Start time is required")
        LocalDateTime startTime,

        @NotNull(message = "End time is required")
        LocalDateTime endTime
) {
}
