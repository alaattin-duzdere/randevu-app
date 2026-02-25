package RandevuApp.domain.business.dto;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
public class ScheduleOverrideRequest {

    @NotNull(message = "İstisna tarihi boş olamaz")
    @FutureOrPresent(message = "Geçmiş bir tarih için istisna oluşturulamaz")
    private LocalDate targetDate;

    @NotNull(message = "Kapalı/Açık durumu belirtilmelidir")
    private Boolean isClosed;

    private LocalTime openTime;
    private LocalTime closeTime;

    private String reason;
}