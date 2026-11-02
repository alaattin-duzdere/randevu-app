package RandevuApp.domain.appointment.availability.controller;

import RandevuApp.domain.appointment.availability.service.IAvailabilityService;
import RandevuApp.domain.appointment.dto.AvailableSlotsResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@RestController
@RequestMapping("/businesses/{businessId}/staff/{staffId}/availability")
@RequiredArgsConstructor
public class AvailabilityController {

    private final IAvailabilityService availabilityService;

    @GetMapping("/slots")
    public AvailableSlotsResponse getAvailableSlotsForStaff(
            @PathVariable Long businessId,
            @PathVariable Long staffId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            @RequestParam int serviceDurationMinutes) {

        return availabilityService.getAvailableSlotsForStaff(businessId, staffId, date, serviceDurationMinutes);
    }

    @GetMapping("/slots/range")
    public AvailableSlotsResponse getAvailableSlotsForDateRange(
            @PathVariable Long businessId,
            @PathVariable Long staffId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @RequestParam int serviceDurationMinutes) {

        return availabilityService.getAvailableSlotsForDateRange(businessId, staffId, startDate, endDate, serviceDurationMinutes);
    }

    @PostMapping("/validate")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void validateSlotAvailability(
            @PathVariable Long businessId,
            @PathVariable Long staffId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime requestedStartTime,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime requestedEndTime,
            @RequestParam(required = false) Long excludeAppointmentId) {

        availabilityService.validateSlotAvailability(
                businessId,
                staffId,
                requestedStartTime,
                requestedEndTime,
                excludeAppointmentId
        );
    }
}
