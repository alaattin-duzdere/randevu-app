package RandevuApp.domain.appointment.dto;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public record AvailableSlotsResponse(
        String timeZone,
        Map<LocalDate, List<TimeSlotResponse>> slots
) {}
