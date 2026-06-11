package RandevuApp.domain.time_off.mapper;

import RandevuApp.domain.time_off.dto.TimeOffResponse;
import RandevuApp.domain.time_off.dto.UpdateTimeOffRequest;
import RandevuApp.domain.time_off.model.TimeOff;
import org.springframework.stereotype.Component;

@Component
public class TimeOffMapper {

    public TimeOffResponse entityToResponse(TimeOff entity) {
        if (entity == null) {
            return null;
        }

        return TimeOffResponse.builder()
                .id(entity.getId())
                .staffId(entity.getStaff() != null ? entity.getStaff().getId() : null)
                .startTime(entity.getStartTime())
                .endTime(entity.getEndTime())
                .type(entity.getType())
                .note(entity.getNote())
                .build();
    }

    public void updateTimeOffFromDto(UpdateTimeOffRequest dto, TimeOff entity) {
        if (dto == null || entity == null) {
            return;
        }

        if (dto.startTime() != null) {
            entity.setStartTime(dto.startTime());
        }

        if (dto.endTime() != null) {
            entity.setEndTime(dto.endTime());
        }

        if (dto.type() != null) {
            entity.setType(dto.type());
        }

        if (dto.note() != null) {
            entity.setNote(dto.note());
        }
    }
}