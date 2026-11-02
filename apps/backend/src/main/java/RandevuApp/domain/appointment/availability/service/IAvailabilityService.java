package RandevuApp.domain.appointment.availability.service;

import RandevuApp.domain.appointment.dto.AvailableSlotsResponse;

import java.time.LocalDate;
import java.time.LocalDateTime;

public interface IAvailabilityService {

    AvailableSlotsResponse getAvailableSlotsForStaff(Long businessId, Long staffId, LocalDate date, int serviceDurationMinutes);

    AvailableSlotsResponse getAvailableSlotsForDateRange(Long businessId, Long staffId, LocalDate startDate, LocalDate endDate, int serviceDurationMinutes);

    void validateSlotAvailability(Long businessId, Long staffId, LocalDateTime requestedStartTime, LocalDateTime requestedEndTime, Long excludeAppointmentId);
}
