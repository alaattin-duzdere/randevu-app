package RandevuApp.domain.business.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalTime;

@Data
public class UpdateBusinessSettingsRequest {

    @NotNull(message = "Slot süresi boş olamaz")
    private Integer slotDurationTime;

    @NotNull(message = "Açılış saati boş olamaz")
    @JsonFormat(pattern = "HH:mm") // JSON'dan "09:00" gelirse LocalTime'a çevirir
    private LocalTime openingTime;

    @NotNull(message = "Kapanış saati boş olamaz")
    @JsonFormat(pattern = "HH:mm")
    private LocalTime closingTime;
}
