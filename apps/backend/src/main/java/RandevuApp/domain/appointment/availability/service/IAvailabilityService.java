package RandevuApp.domain.appointment.availability.service;

import RandevuApp.domain.appointment.availability.model.TimeSlot;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public interface IAvailabilityService {

    /**
     * 1. ANA LİSTELEME METODU
     * Müşteri bir hizmet ve personel seçtiğinde, o güne ait seçilebilir saatleri döndürür.
     * * İşleyiş:
     * - IBusinessScheduleDomainService.getEffectiveOperatingHours çağrılır.
     * - ITimeOffDomainService.getStaffTimeOffsBetween çağrılır.
     * - AppointmentRepository üzerinden o günkü randevular çekilir.
     * - Tüm "dolu" zamanlar birleştirilir ve boşluklar hesaplanır.
     */
    List<TimeSlot> getAvailableSlotsForStaff(Long businessId, Long staffId, LocalDate date, int serviceDurationMinutes);

    /**
     * 2. HAFTALIK / ÇOKLU GÜN GÖRÜNÜMÜ (Opsiyonel ama UI için çok faydalı)
     * Belirli bir tarih aralığındaki günlerin uygunluk durumunu Map olarak döndürür.
     * Örneğin: { "2026-03-10": [10:00, 10:30], "2026-03-11": [14:00, 15:00] }
     */
    Map<LocalDate, List<TimeSlot>> getAvailableSlotsForDateRange(Long businessId, Long staffId, LocalDate startDate, LocalDate endDate, int serviceDurationMinutes);

    /**
     * 3. KESİN DOĞRULAMA (VALIDATION) METODU
     * AppointmentService randevuyu kaydetmeden saniyeler önce bu metodu çağırır.
     * "Bu saat aralığı bu personel için gerçekten uygun mu?" sorusunu yanıtlar.
     * * Not: "excludeAppointmentId" parametresi Reschedule (yeniden planlama) işlemi içindir.
     * Mevcut randevuyu kendi kendine çakışıyor gibi görmemek için kullanılır.
     */
    void validateSlotAvailability(Long businessId, Long staffId, LocalDateTime requestedStartTime, LocalDateTime requestedEndTime, Long excludeAppointmentId);
}
