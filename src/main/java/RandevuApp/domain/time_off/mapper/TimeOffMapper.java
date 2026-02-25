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

        if (dto.getStartTime() != null) {
            entity.setStartTime(dto.getStartTime());
        }

        if (dto.getEndTime() != null) {
            entity.setEndTime(dto.getEndTime());
        }

        if (dto.getType() != null) {
            entity.setType(dto.getType());
        }

        if (dto.getNote() != null) {
            entity.setNote(dto.getNote());
        }
    }
}