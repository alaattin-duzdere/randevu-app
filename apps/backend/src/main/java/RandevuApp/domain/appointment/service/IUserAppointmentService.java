package RandevuApp.domain.appointment.service;

import RandevuApp.domain.appointment.dto.AppointmentResponse;
import RandevuApp.domain.appointment.dto.UserCreateAppointmentRequest;
import RandevuApp.domain.appointment.dto.UserAppointmentFilter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface IUserAppointmentService {

    AppointmentResponse createAppointment(Long currentUserId, UserCreateAppointmentRequest request);

    AppointmentResponse getMyAppointment(Long currentUserId, Long appointmentId);

    Page<AppointmentResponse> getMyAppointments(Long currentUserId, UserAppointmentFilter filter, Pageable pageable);
}
