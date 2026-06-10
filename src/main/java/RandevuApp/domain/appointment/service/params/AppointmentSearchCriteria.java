package RandevuApp.domain.appointment.service.params;

import RandevuApp.domain.appointment.model.AppointmentStatus;

import java.time.LocalDateTime;
import java.util.List;

public record AppointmentSearchCriteria(
        Long userId,
        Long staffId,
        Long businessId,
        Long serviceId,
        List<AppointmentStatus> statuses,
        LocalDateTime startDate,
        LocalDateTime endDate
) {
}