package RandevuApp.domain.appointment.availability.service.impl;

import RandevuApp.commons.util.TimeZoneUtil;
import RandevuApp.config.BusinessProperties;
import RandevuApp.domain.appointment.availability.mapper.TimeSlotMapper;
import RandevuApp.domain.appointment.availability.model.TimeSlot;
import RandevuApp.domain.appointment.availability.service.IAvailabilityDomainService;
import RandevuApp.domain.appointment.availability.service.IAvailabilityService;
import RandevuApp.domain.appointment.dto.AvailableSlotsResponse;
import RandevuApp.domain.appointment.dto.TimeSlotResponse;
import RandevuApp.domain.business.model.Business;
import RandevuApp.domain.business.service.IBusinessDomainService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class AvailabilityServiceImpl implements IAvailabilityService {

    private final IAvailabilityDomainService availabilityDomainService;
    private final IBusinessDomainService businessDomainService;
    private final BusinessProperties businessProperties;
    private final TimeSlotMapper timeSlotMapper;

    @Override
    public AvailableSlotsResponse getAvailableSlotsForStaff(Long businessId, Long staffId, LocalDate date, int serviceDurationMinutes) {
        ZoneId zoneId = getBusinessZoneId(businessId);
        List<TimeSlot> domainSlots = availabilityDomainService.getAvailableSlotsForStaff(businessId, staffId, date, serviceDurationMinutes);
        List<TimeSlotResponse> mappedSlots = domainSlots.stream()
                .map(slot -> timeSlotMapper.toResponse(slot, zoneId))
                .toList();
        return new AvailableSlotsResponse(zoneId.getId(), Map.of(date, mappedSlots));
    }

    @Override
    public AvailableSlotsResponse getAvailableSlotsForDateRange(Long businessId, Long staffId, LocalDate startDate, LocalDate endDate, int serviceDurationMinutes) {
        ZoneId zoneId = getBusinessZoneId(businessId);
        Map<LocalDate, List<TimeSlot>> domainSlots = availabilityDomainService.getAvailableSlotsForDateRange(
                businessId, staffId, startDate, endDate, serviceDurationMinutes);
        Map<LocalDate, List<TimeSlotResponse>> mappedSlots = timeSlotMapper.toResponse(domainSlots, zoneId);
        return new AvailableSlotsResponse(zoneId.getId(), mappedSlots);
    }

    @Override
    public void validateSlotAvailability(Long businessId, Long staffId, LocalDateTime requestedStartTime, LocalDateTime requestedEndTime, Long excludeAppointmentId) {
        availabilityDomainService.validateSlotAvailability(businessId, staffId, requestedStartTime, requestedEndTime, excludeAppointmentId);
    }

    private ZoneId getBusinessZoneId(Long businessId) {
        Business business = businessDomainService.getById(businessId);
        return TimeZoneUtil.resolve(business.getTimeZone(), businessProperties.getDefaultTimezone());
    }
}
