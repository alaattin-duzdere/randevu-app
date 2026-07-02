package RandevuApp.domain.appointment.availability.model;

import java.time.LocalDateTime;

public record TimeSlot(
        LocalDateTime startTime,
        LocalDateTime endTime
) {
}
