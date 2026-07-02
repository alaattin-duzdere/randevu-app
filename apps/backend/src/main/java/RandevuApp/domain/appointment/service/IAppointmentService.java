package RandevuApp.domain.appointment.service;

import RandevuApp.domain.appointment.dto.AppointmentResponse;
import RandevuApp.domain.appointment.dto.CreateAppointmentRequest;
import RandevuApp.domain.appointment.dto.RescheduleAppointmentRequest;
import RandevuApp.domain.appointment.model.AppointmentStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;

public interface IAppointmentService {

    // Write
    /**
     * Müşteri veya sistem tarafından yeni bir randevu oluşturulması.
     * Orkestrasyon:
     * 1. AvailabilityService ile saatlerin boş olduğu doğrulanır.
     * 2. DomainService ile Appointment entity'si üretilir.
     * 3. Repository ile veritabanına kaydedilir.
     * 4. (Opsiyonel) Müşteriye onay e-postası atılır.
     */
    AppointmentResponse createAppointment(CreateAppointmentRequest command);

    /**
     * Mevcut bir randevunun tarihinin/saatinin değiştirilmesi.
     * Orkestrasyon: AvailabilityService ile yeni saatin uygunluğu 'excludeAppointmentId' kullanılarak denetlenir.
     */
    AppointmentResponse rescheduleAppointment(Long appointmentId,RescheduleAppointmentRequest command);

    /**
     * Müşterinin kendi geçmiş ve gelecek randevularını listelemesi.
     */
    Page<AppointmentResponse> getAppointmentsByUserId(Long userId, Pageable pageable);

    /**
     * Randevu statüsünün güncellenmesi (Örn: İşletme onayladı, Müşteri iptal etti vb.)
     * Orkestrasyon: DomainService içindeki statü geçiş kuralları (State Machine) işletilir.
     */
    AppointmentResponse updateAppointmentStatus(Long appointmentId, AppointmentStatus newStatus);
    // Read

    /**
     * Tek bir randevunun detaylarının getirilmesi.
     */
    AppointmentResponse getAppointmentById(Long appointmentId);

    /**
     * Personelin veya işletmenin belirli bir takvim aralığındaki randevuları listelemesi.
     */
    Page<AppointmentResponse> getAppointmentsByStaffAndDateRange(Long staffId, LocalDateTime startDate, LocalDateTime endDate, Pageable pageable);
}
