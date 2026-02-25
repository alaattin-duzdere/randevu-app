package RandevuApp.domain.business.controller;

import RandevuApp.api.CustomResponseBody;
import RandevuApp.domain.business.dto.OperatingHourDto;
import RandevuApp.domain.business.dto.ScheduleOverrideRequest;
import RandevuApp.domain.business.dto.ScheduleOverrideResponse;
import RandevuApp.domain.business.dto.UpdateOperatingHoursRequest;
import RandevuApp.domain.business.service.IBusinessScheduleService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<CustomResponseBody<List<OperatingHourDto>>> getOperatingHours(
            @PathVariable Long businessId) {

        List<OperatingHourDto> hours = scheduleService.getOperatingHours(businessId);
        return ResponseEntity.ok(CustomResponseBody.ok(hours, "Operating hours retrieved successfully"));
    }

    @PutMapping("/operating-hours")
    public ResponseEntity<CustomResponseBody<List<OperatingHourDto>>> updateOperatingHours(
            @PathVariable Long businessId,
            @Valid @RequestBody UpdateOperatingHoursRequest request) {

        Long ownerId = getCurrentUserId();
        List<OperatingHourDto> updatedHours = scheduleService.updateOperatingHours(businessId, request.getOperatingHours(), ownerId);
        return ResponseEntity.ok(CustomResponseBody.ok(updatedHours, "Operating hours updated successfully"));
    }

    // OVERRIDES

    @PostMapping("/overrides")
    public ResponseEntity<CustomResponseBody<ScheduleOverrideResponse>> createOverride(
            @PathVariable Long businessId,
            @Valid @RequestBody ScheduleOverrideRequest request) {

        Long ownerId = getCurrentUserId();
        ScheduleOverrideResponse response = scheduleService.createOverride(businessId, request, ownerId);
        return ResponseEntity.status(HttpStatus.CREATED).body(CustomResponseBody.ok(response, "Schedule override created successfully"));
    }

    @PutMapping("/overrides/{overrideId}")
    public ResponseEntity<CustomResponseBody<ScheduleOverrideResponse>> updateOverride(
            @PathVariable Long businessId,
            @PathVariable Long overrideId,
            @Valid @RequestBody ScheduleOverrideRequest request) {

        Long ownerId = getCurrentUserId();
        ScheduleOverrideResponse response = scheduleService.updateOverride(businessId, overrideId, request, ownerId);
        return ResponseEntity.ok(CustomResponseBody.ok(response, "Schedule override updated successfully"));
    }

    @DeleteMapping("/overrides/{overrideId}")
    public ResponseEntity<CustomResponseBody<Void>> deleteOverride(
            @PathVariable Long businessId,
            @PathVariable Long overrideId) {

        Long ownerId = getCurrentUserId();
        scheduleService.deleteOverride(businessId, overrideId, ownerId);
        return ResponseEntity.ok(CustomResponseBody.ok(null, "Schedule override deleted successfully"));
    }

    @GetMapping("/overrides")
    public ResponseEntity<CustomResponseBody<List<ScheduleOverrideResponse>>> getOverridesInDateRange(
            @PathVariable Long businessId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {

        List<ScheduleOverrideResponse> overrides = scheduleService.getOverridesInDateRange(businessId, startDate, endDate);
        return ResponseEntity.ok(CustomResponseBody.ok(overrides, "Schedule overrides retrieved successfully"));
    }
}
