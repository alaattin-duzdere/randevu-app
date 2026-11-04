package RandevuApp.domain.appointment.service;

import RandevuApp.domain.appointment.model.Appointment;
import RandevuApp.domain.appointment.model.AppointmentStatus;
import RandevuApp.domain.appointment.service.params.AppointmentFindCriteria;
import RandevuApp.domain.appointment.service.params.CreateAppointmentParams;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;

public interface IAppointmentDomainService {
    Appointment createEntity(CreateAppointmentParams params);

    void reschedule(Appointment appointment, LocalDateTime newStartTime, LocalDateTime newEndTime);

    void transitionStatus(Appointment appointment, AppointmentStatus newStatus);

    // wrappers for repository methods
    Appointment save(Appointment appointment);

    Appointment getById(Long appointmentId);

    Page<Appointment> findAppointmentByCriteria(AppointmentFindCriteria criteria, Pageable pageable);

    boolean existsByCriteria(AppointmentFindCriteria criteria);

    List<Appointment> findActiveAppointmentsByStaffAndFrame(Long staffId, LocalDateTime openTime, LocalDateTime closeTime, List<AppointmentStatus> activeStatuses);

    List<Appointment> findConflictingAppointments(Long staffId, LocalDateTime requestedStartTime, LocalDateTime requestedEndTime, List<AppointmentStatus> activeStatuses);
}
