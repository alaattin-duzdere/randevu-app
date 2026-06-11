package RandevuApp.domain.business.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotNull;

import java.time.LocalTime;

public record UpdateBusinessSettingsRequest(
        @NotNull(message = "Slot süresi boş olamaz") Integer slotDurationTime,

        @NotNull(message = "Açılış saati boş olamaz") @JsonFormat(pattern = "HH:mm") LocalTime openingTime,

        @NotNull(message = "Kapanış saati boş olamaz") @JsonFormat(pattern = "HH:mm") LocalTime closingTime
) {}
