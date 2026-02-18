package RandevuApp.domain.business.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BusinessSettingsResponse {
    private Integer slotDurationTime;
    private String openingTime;
    private String closingTime;
    private Long businessId;
}
