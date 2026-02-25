package RandevuApp.domain.time_off.dto;

import RandevuApp.domain.time_off.model.TimeOffType;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class UpdateTimeOffRequest {
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private TimeOffType type;
    private String note;
}