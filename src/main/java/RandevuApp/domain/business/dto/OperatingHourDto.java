package RandevuApp.domain.business.dto;

import jakarta.validation.constraints.NotNull;

import java.time.DayOfWeek;
import java.time.LocalTime;

public record OperatingHourDto(
        Long id,
        @NotNull(message = "Gün bilgisi boş olamaz") DayOfWeek dayOfWeek,
        @NotNull(message = "Kapalı/Açık durumu belirtilmelidir") Boolean isClosed,
        LocalTime openTime,
        LocalTime closeTime
) {}
