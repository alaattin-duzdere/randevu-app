package RandevuApp.test;

import RandevuApp.domain.appointment.availability.model.TimeSlot;
import RandevuApp.domain.appointment.availability.service.impl.AvailabilityServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class AppointmentTestController {

    private final AvailabilityServiceImpl availabilityService;

    @GetMapping("/test/availability")
    public List<TimeSlot> getAvailableSlotsForStaff(
            @RequestParam Long businessId,
            @RequestParam Long staffId,
            @RequestParam LocalDate date,
            @RequestParam int serviceDurationMinutes
    ){
        return availabilityService.getAvailableSlotsForStaff(businessId, staffId, date, serviceDurationMinutes);
    }

    @GetMapping("/test/availability/range")
    public Map<LocalDate, List<TimeSlot>> getAvailableSlotsForDateRange(
            @RequestParam Long businessId,
            @RequestParam Long staffId,
            @RequestParam LocalDate startDate,
            @RequestParam LocalDate endDate,
            @RequestParam int serviceDurationMinutes
    ){
        return availabilityService.getAvailableSlotsForDateRange(businessId, staffId, startDate, endDate, serviceDurationMinutes);
    }

    @GetMapping("/test/validate")
    public String validateSlotAvailability(
            @RequestParam Long businessId,
            @RequestParam Long staffId,
            @RequestParam LocalDateTime requestedStartTime,
            @RequestParam LocalDateTime requestedEndTime,
            @RequestParam Long excludeAppointmentId
    ) {
        availabilityService.validateSlotAvailability(businessId,staffId,requestedStartTime,requestedEndTime,excludeAppointmentId);
        return "Be Happy! Available Slot.";
    }
 }
