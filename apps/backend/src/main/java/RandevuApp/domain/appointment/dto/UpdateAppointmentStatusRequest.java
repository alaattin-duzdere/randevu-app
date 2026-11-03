package RandevuApp.domain.appointment.dto;

import RandevuApp.domain.appointment.model.AppointmentStatus;
import jakarta.validation.constraints.NotNull;

public record UpdateAppointmentStatusRequest(
        @NotNull(message = "Appointment status is required")
        AppointmentStatus status
) {
}
