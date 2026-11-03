package RandevuApp.domain.appointment.service;

import RandevuApp.domain.appointment.dto.AppointmentResponse;
import RandevuApp.domain.appointment.dto.BusinessAppointmentFilter;
import RandevuApp.domain.appointment.dto.RescheduleAppointmentRequest;
import RandevuApp.domain.appointment.model.AppointmentStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface IBusinessOwnerAppointmentService {
    AppointmentResponse get(Long businessId, Long appointmentId);

    Page<AppointmentResponse> list(Long businessId, BusinessAppointmentFilter filter, Pageable pageable);

    AppointmentResponse reschedule(Long businessId, Long appointmentId, RescheduleAppointmentRequest request);

    AppointmentResponse updateStatus(Long businessId, Long appointmentId, AppointmentStatus newStatus);
}
