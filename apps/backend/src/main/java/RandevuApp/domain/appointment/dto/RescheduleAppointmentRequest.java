package RandevuApp.domain.appointment.dto;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class RescheduleAppointmentRequest {

    @NotNull(message = "Yeni başlangıç zamanı boş olamaz")
    @Future(message = "Yeni saat geçmiş bir tarih olamaz")
    private LocalDateTime newStartTime;

    @NotNull(message = "Yeni bitiş zamanı boş olamaz")
    @Future(message = "Yeni saat geçmiş bir tarih olamaz")
    private LocalDateTime newEndTime;
}
