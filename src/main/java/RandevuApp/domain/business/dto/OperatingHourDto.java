package RandevuApp.domain.business.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.DayOfWeek;
import java.time.LocalTime;

@Data
public class OperatingHourDto {

    private Long id;

    @NotNull(message = "Gün bilgisi boş olamaz")
    private DayOfWeek dayOfWeek;

    @NotNull(message = "Kapalı/Açık durumu belirtilmelidir")
    private Boolean isClosed;

    private LocalTime openTime;
    private LocalTime closeTime;
}