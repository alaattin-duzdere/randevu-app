package RandevuApp.domain.appointment.service.params;

import RandevuApp.domain.appointment.model.AppointmentStatus;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.List;

@Builder
public record AppointmentFindCriteria(
        Long userId,
        Long staffId,
        Long businessId,
        Long serviceId,
        List<AppointmentStatus> statuses,
        LocalDateTime startDate,
        LocalDateTime endDate
) {
}