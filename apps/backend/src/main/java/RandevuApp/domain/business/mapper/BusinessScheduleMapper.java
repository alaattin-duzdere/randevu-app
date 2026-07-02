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
        return new OperatingHourDto(
                entity.getId(),
                entity.getDayOfWeek(),
                entity.isClosed(),
                entity.getOpenTime(),
                entity.getCloseTime()
        );
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
        if (dto.targetDate() != null) entity.setTargetDate(dto.targetDate());
        if (dto.isClosed() != null) entity.setClosed(dto.isClosed());

        entity.setOpenTime(dto.openTime());
        entity.setCloseTime(dto.closeTime());

        if (dto.reason() != null) entity.setReason(dto.reason());
    }
}