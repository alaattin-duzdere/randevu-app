package RandevuApp.domain.time_off.dto;

import RandevuApp.domain.time_off.model.TimeOffType;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class TimeOffResponse {
    private Long id;
    private Long staffId;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private TimeOffType type;
    private String note;
}