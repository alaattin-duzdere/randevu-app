package RandevuApp.domain.appointment.availability.mapper;

import RandevuApp.commons.util.TimeConverter;
import RandevuApp.domain.appointment.availability.model.TimeSlot;
import RandevuApp.domain.appointment.dto.TimeSlotResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class TimeSlotMapper {

    private final TimeConverter converter;

    public TimeSlotResponse toResponse(TimeSlot slot, ZoneId zone) {
        return new TimeSlotResponse(
                converter.toOffset(slot.startTime(), zone),
                converter.toOffset(slot.endTime(), zone)
        );
    }

    public Map<LocalDate, List<TimeSlotResponse>> toResponse(
            Map<LocalDate, List<TimeSlot>> slots, ZoneId zone) {

        Map<LocalDate, List<TimeSlotResponse>> result = new LinkedHashMap<>();
        slots.forEach((date, list) ->
                result.put(date, list.stream().map(s -> toResponse(s, zone)).toList()));
        return result;
    }
}
