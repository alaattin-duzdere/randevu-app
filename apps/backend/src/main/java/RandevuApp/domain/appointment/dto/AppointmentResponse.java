package RandevuApp.domain.appointment.dto;

import RandevuApp.domain.appointment.model.AppointmentStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record AppointmentResponse(
        Long id,
        LocalDateTime startTime,
        LocalDateTime endTime,
        BigDecimal price,
        AppointmentStatus appointmentStatus,
        Long businessId,
        String businessName,
        Long staffId,
        String staffName,
        Long serviceId,
        String serviceName,
        String customerName,
        String customerLastName,
        String customerPhone
) {
}
