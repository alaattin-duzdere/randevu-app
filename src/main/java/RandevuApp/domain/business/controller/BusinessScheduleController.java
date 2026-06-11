package RandevuApp.domain.business.controller;

import RandevuApp.domain.business.dto.OperatingHourDto;
import RandevuApp.domain.business.dto.ScheduleOverrideRequest;
import RandevuApp.domain.business.dto.ScheduleOverrideResponse;
import RandevuApp.domain.business.dto.UpdateOperatingHoursRequest;
import RandevuApp.domain.business.service.IBusinessScheduleService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

import static RandevuApp.utils.SecurityUtils.getCurrentUserId;

@RestController
@RequestMapping("/api/businesses/{businessId}/schedule")
@RequiredArgsConstructor
public class BusinessScheduleController {

    private final IBusinessScheduleService scheduleService;

    // OPERATING HOURS

    @GetMapping("/operating-hours")
    public List<OperatingHourDto> getOperatingHours(
            @PathVariable Long businessId) {

        return scheduleService.getOperatingHours(businessId);
    }

    @PutMapping("/operating-hours")
    public List<OperatingHourDto> updateOperatingHours(
            @PathVariable Long businessId,
            @Valid @RequestBody UpdateOperatingHoursRequest request) {

        Long ownerId = getCurrentUserId();
        return scheduleService.updateOperatingHours(businessId, request.operatingHours(), ownerId);
    }

    // OVERRIDES

    @PostMapping("/overrides")
    @ResponseStatus(HttpStatus.CREATED)
    public ScheduleOverrideResponse createOverride(
            @PathVariable Long businessId,
            @Valid @RequestBody ScheduleOverrideRequest request) {

        Long ownerId = getCurrentUserId();
        return scheduleService.createOverride(businessId, request, ownerId);
    }

    @PutMapping("/overrides/{overrideId}")
    public ScheduleOverrideResponse updateOverride(
            @PathVariable Long businessId,
            @PathVariable Long overrideId,
            @Valid @RequestBody ScheduleOverrideRequest request) {

        Long ownerId = getCurrentUserId();
        return scheduleService.updateOverride(businessId, overrideId, request, ownerId);
    }

    @DeleteMapping("/overrides/{overrideId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteOverride(
            @PathVariable Long businessId,
            @PathVariable Long overrideId) {

        Long ownerId = getCurrentUserId();
        scheduleService.deleteOverride(businessId, overrideId, ownerId);
    }

    @GetMapping("/overrides")
    public List<ScheduleOverrideResponse> getOverridesInDateRange(
            @PathVariable Long businessId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {

        return scheduleService.getOverridesInDateRange(businessId, startDate, endDate);
    }
}
