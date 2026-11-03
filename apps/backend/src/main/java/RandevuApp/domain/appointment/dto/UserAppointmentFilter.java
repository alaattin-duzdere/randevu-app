package RandevuApp.domain.appointment.dto;

import RandevuApp.domain.appointment.model.AppointmentStatus;

import java.time.LocalDateTime;
import java.util.List;

public record UserAppointmentFilter(
        Long staffId,
        Long businessId,
        Long serviceId,
        List<AppointmentStatus> statuses,
        LocalDateTime startDate,
        LocalDateTime endDate
) {
}
