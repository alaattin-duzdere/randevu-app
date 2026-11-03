package RandevuApp.domain.appointment.mapper;

import RandevuApp.domain.appointment.dto.AppointmentResponse;
import RandevuApp.domain.appointment.model.Appointment;
import org.springframework.stereotype.Component;

@Component
public class AppointmentMapper {

    public AppointmentResponse mapToResponse(Appointment appointment) {
        return new AppointmentResponse(
                appointment.getId(),
                appointment.getStartTime(),
                appointment.getEndTime(),
                appointment.getPrice(),
                appointment.getAppointmentStatus(),
                appointment.getBusiness() != null ? appointment.getBusiness().getId() : null,
                appointment.getBusiness() != null ? appointment.getBusiness().getName() : null,
                appointment.getStaff() != null ? appointment.getStaff().getId() : null,
                appointment.getStaff() != null ? appointment.getStaff().getName() : null,
                appointment.getService() != null ? appointment.getService().getId() : null,
                appointment.getService() != null ? appointment.getService().getName() : null,
                appointment.getCustomerName(),
                appointment.getCustomerLastName(),
                appointment.getCustomerPhone()
        );
    }
}
