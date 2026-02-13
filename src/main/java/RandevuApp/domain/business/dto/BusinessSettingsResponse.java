package RandevuApp.domain.business.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BusinessSettingsResponse {

    private Long id;

    private Integer slotDurationTime;

    private String openingTime;

    private String closingTime;

    private String timeZone;

    private Long businessId;
}
