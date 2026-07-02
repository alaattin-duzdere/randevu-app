package RandevuApp.domain.appointment.dto;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CreateAppointmentRequest {
    @NotNull(message = "Kullanıcı ID boş olamaz")
    private Long userId;

    @NotNull(message = "İşletme ID boş olamaz")
    private Long businessId;

    @NotNull(message = "Personel ID boş olamaz")
    private Long staffId;

    @NotNull(message = "Hizmet ID boş olamaz")
    private Long serviceId;

    @NotNull(message = "Başlangıç zamanı boş olamaz")
    @Future(message = "Randevu geçmiş bir tarihe alınamaz")
    private LocalDateTime startTime;

    @NotNull(message = "Bitiş zamanı boş olamaz")
    @Future(message = "Randevu bitiş zamanı geçmiş olamaz")
    private LocalDateTime endTime;

    @NotBlank(message = "Müşteri adı boş olamaz")
    private String customerName;

    @NotBlank(message = "Müşteri telefonu boş olamaz")
    private String customerPhone;
}
