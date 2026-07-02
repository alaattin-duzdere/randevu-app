package RandevuApp.domain.time_off.service.params;

import RandevuApp.domain.staff.model.Staff;
import RandevuApp.domain.time_off.model.TimeOffType;

import java.time.LocalDateTime;

public record CreateTimeOffParams(
        Staff staff,
        LocalDateTime startTime,
        LocalDateTime endTime,
        TimeOffType type,
        String note
) {
}
