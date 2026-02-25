package RandevuApp.domain.business.service;

import RandevuApp.domain.business.dto.OperatingHourDto;
import RandevuApp.domain.business.dto.ScheduleOverrideRequest;
import RandevuApp.domain.business.dto.ScheduleOverrideResponse;

import java.time.LocalDate;
import java.util.List;

public interface IBusinessScheduleService {

    List<OperatingHourDto> getOperatingHours(Long businessId);

    List<OperatingHourDto> updateOperatingHours(Long businessId, List<OperatingHourDto> requests, Long ownerId);


    // Exception Days
    ScheduleOverrideResponse createOverride(Long businessId, ScheduleOverrideRequest request, Long ownerId);

    ScheduleOverrideResponse updateOverride(Long businessId, Long overrideId, ScheduleOverrideRequest request, Long ownerId);

    void deleteOverride(Long businessId, Long overrideId, Long ownerId);

    // For Appointment
    List<ScheduleOverrideResponse> getOverridesInDateRange(Long businessId, LocalDate startDate, LocalDate endDate);

}