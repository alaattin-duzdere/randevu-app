package RandevuApp.domain.business.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalTime;

@Getter
@Setter
public class BusinessSettingsResponse {
    private Integer slotDurationTime;
    private LocalTime openingTime;
    private LocalTime closingTime;
    private Long businessId;
}
