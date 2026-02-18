package RandevuApp.domain.business.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class UpdateBusinessSettingsRequest {

    @NotNull(message = "Slot süresi boş olamaz")
    private Integer slotDurationTime;

    @NotNull(message = "Açılış saati boş olamaz")
    @Pattern(regexp = "^([0-1]?[0-9]|2[0-3]):[0-5][0-9]$", message = "Saat formatı HH:mm olmalıdır")
    private String openingTime;

    @NotNull(message = "Kapanış saati boş olamaz")
    @Pattern(regexp = "^([0-1]?[0-9]|2[0-3]):[0-5][0-9]$", message = "Saat formatı HH:mm olmalıdır")
    private String closingTime;
}
