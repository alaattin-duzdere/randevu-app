package RandevuApp.domain.appointment.service;

import RandevuApp.domain.appointment.model.Appointment;
import RandevuApp.domain.appointment.model.AppointmentStatus;
import RandevuApp.domain.appointment.service.params.AppointmentSearchCriteria;
import RandevuApp.domain.appointment.service.params.CreateAppointmentParams;
import RandevuApp.domain.business.model.Business;
import RandevuApp.domain.service_offering.model.ServiceOffering;
import RandevuApp.domain.staff.model.Staff;
import RandevuApp.domain.user.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.time.LocalDateTime;

public interface IAppointmentDomainService {
    Appointment createEntity(CreateAppointmentParams params);

    void validateAppointmentDuration(ServiceOffering service, LocalDateTime startTime, LocalDateTime endTime);


    /**
     * Randevunun statüsünü güvenli bir şekilde değiştirir.
     * Örneğin: "CANCELLED statüsündeki bir randevu doğrudan COMPLETED yapılamaz"
     * gibi statü geçiş (transition) kurallarını burada denetler.
     */
    void transitionStatus(Appointment appointment, AppointmentStatus newStatus);

    /**
     * Randevunun zamanını günceller.
     * (Not: Saatlerin boş olup olmadığı Application Service tarafından AvailabilityService
     * kullanılarak zaten doğrulanmış olmalıdır. Bu metod sadece nesnenin iç durumunu değiştirir).
     */
    void reschedule(Appointment appointment, LocalDateTime newStartTime, LocalDateTime newEndTime);


    /// --- 5. WRAPPERS & SPECIFICATION QUERIES ---
    Appointment save(Appointment appointment);

    Appointment getById(Long appointmentId);

    Page<Appointment> searchAppointments(AppointmentSearchCriteria criteria, Pageable pageable);

    boolean existsByCriteria(AppointmentSearchCriteria criteria);
}
