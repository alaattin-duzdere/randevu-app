package RandevuApp.domain.appointment.dto;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;

import java.time.OffsetDateTime;

public record UserCreateAppointmentRequest(
        @NotNull(message = "Business ID is required")
        Long businessId,
        @NotNull(message = "Staff ID is required")
        Long staffId,
        @NotNull(message = "Service ID is required")
        Long serviceId,
        @NotNull(message = "Start time is required")
        @Future(message = "Appointment start time must be in the future")
        OffsetDateTime startTime
) {
    public CreateAppointmentCommand toCommand(Long userId) {
        return new CreateAppointmentCommand(userId, businessId, staffId, serviceId, startTime);
    }
}
