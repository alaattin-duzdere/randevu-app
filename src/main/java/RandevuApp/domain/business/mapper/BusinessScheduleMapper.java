package RandevuApp.domain.business.mapper;

import RandevuApp.domain.business.dto.OperatingHourDto;
import RandevuApp.domain.business.dto.ScheduleOverrideRequest;
import RandevuApp.domain.business.dto.ScheduleOverrideResponse;
import RandevuApp.domain.business.model.BusinessOperatingHour;
import RandevuApp.domain.business.model.BusinessScheduleOverride;
import org.springframework.stereotype.Component;

@Component
public class BusinessScheduleMapper {

    public OperatingHourDto toOperatingHourDto(BusinessOperatingHour entity) {
        if (entity == null) return null;

        OperatingHourDto dto = new OperatingHourDto();
        dto.setId(entity.getId());
        dto.setDayOfWeek(entity.getDayOfWeek());
        dto.setIsClosed(entity.isClosed());
        dto.setOpenTime(entity.getOpenTime());
        dto.setCloseTime(entity.getCloseTime());
        return dto;
    }

    public ScheduleOverrideResponse toOverrideResponse(BusinessScheduleOverride entity) {
        if (entity == null) return null;

        return ScheduleOverrideResponse.builder()
                .id(entity.getId())
                .targetDate(entity.getTargetDate())
                .isClosed(entity.isClosed())
                .openTime(entity.getOpenTime())
                .closeTime(entity.getCloseTime())
                .reason(entity.getReason())
                .build();
    }

    public void updateOverrideFromDto(ScheduleOverrideRequest dto, BusinessScheduleOverride entity) {
        if (dto == null || entity == null) return;

        if (dto.getTargetDate() != null) entity.setTargetDate(dto.getTargetDate());
        if (dto.getIsClosed() != null) entity.setClosed(dto.getIsClosed());

        entity.setOpenTime(dto.getOpenTime());
        entity.setCloseTime(dto.getCloseTime());

        if (dto.getReason() != null) entity.setReason(dto.getReason());
    }
}