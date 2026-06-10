package RandevuApp.domain.appointment.dto;

import RandevuApp.domain.appointment.model.AppointmentStatus;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class AppointmentResponse {
    private Long id;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private BigDecimal price;
    private AppointmentStatus appointmentStatus;

    private Long businessId;
    private String businessName;

    private Long staffId;
    private String staffName;

    private Long serviceId;
    private String serviceName;

    private String customerName;
    private String customerPhone;
}
