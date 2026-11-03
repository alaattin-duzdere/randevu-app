package RandevuApp.domain.appointment.service;

import RandevuApp.domain.appointment.dto.AdminCreateAppointmentRequest;
import RandevuApp.domain.appointment.dto.AppointmentResponse;
import RandevuApp.domain.appointment.dto.RescheduleAppointmentRequest;
import RandevuApp.domain.appointment.model.AppointmentStatus;
import RandevuApp.domain.appointment.service.params.AppointmentFindCriteria;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface IAdminAppointmentService {
    AppointmentResponse createAppointment(AdminCreateAppointmentRequest request);

    void rescheduleAppointment(Long appointmentId, RescheduleAppointmentRequest request);

    void updateAppointmentStatus(Long appointmentId, AppointmentStatus newStatus);

    AppointmentResponse getAppointment(Long appointmentId);

    Page<AppointmentResponse> getAppointments(AppointmentFindCriteria criteria, Pageable pageable);
}
