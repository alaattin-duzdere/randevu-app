package RandevuApp.domain.business.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@Builder
public class ScheduleOverrideResponse {
    private Long id;
    private LocalDate targetDate;
    private Boolean isClosed;
    private LocalTime openTime;
    private LocalTime closeTime;
    private String reason;
}