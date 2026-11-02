package RandevuApp.domain.appointment.dto;

import java.time.OffsetDateTime;

public record TimeSlotResponse(
        OffsetDateTime startTime,
        OffsetDateTime endTime
) {}
