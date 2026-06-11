package RandevuApp.domain.business.dto;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.time.LocalTime;

public record ScheduleOverrideRequest(
        @NotNull(message = "İstisna tarihi boş olamaz")
        @FutureOrPresent(message = "Geçmiş bir tarih için istisna oluşturulamaz") LocalDate targetDate,

        @NotNull(message = "Kapalı/Açık durumu belirtilmelidir") Boolean isClosed,

        LocalTime openTime,
        LocalTime closeTime,

        String reason
) {}
