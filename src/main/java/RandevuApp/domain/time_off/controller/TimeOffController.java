package RandevuApp.domain.time_off.controller;

import RandevuApp.api.CustomResponseBody;
import RandevuApp.domain.time_off.dto.CreateTimeOffRequest;
import RandevuApp.domain.time_off.dto.TimeOffResponse;
import RandevuApp.domain.time_off.dto.UpdateTimeOffRequest;
import RandevuApp.domain.time_off.service.ITimeOffService;
import RandevuApp.utils.SecurityUtils;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/businesses/{businessId}/staffs/{staffId}/time-offs")
@RequiredArgsConstructor
public class TimeOffController {

    private final ITimeOffService timeOffService;

    @PostMapping
    public ResponseEntity<CustomResponseBody<TimeOffResponse>> createTimeOff(
            @PathVariable Long businessId,
            @PathVariable Long staffId,
            @Valid @RequestBody CreateTimeOffRequest request) {
        
        Long ownerId = SecurityUtils.getCurrentUserId();
        TimeOffResponse response = timeOffService.createTimeOff(businessId, staffId, request, ownerId);
        return ResponseEntity.status(HttpStatus.CREATED).body(CustomResponseBody.ok(response, "Time off created successfully"));
    }

    @PutMapping("/{timeOffId}")
    public ResponseEntity<CustomResponseBody<TimeOffResponse>> updateTimeOff(
            @PathVariable Long businessId,
            @PathVariable Long staffId,
            @PathVariable Long timeOffId,
            @Valid @RequestBody UpdateTimeOffRequest request) {

        Long ownerId = SecurityUtils.getCurrentUserId();
        TimeOffResponse response = timeOffService.updateTimeOff(businessId, staffId, timeOffId, request, ownerId);
        return ResponseEntity.ok(CustomResponseBody.ok(response, "Time off updated successfully"));
    }

    @DeleteMapping("/{timeOffId}")
    public ResponseEntity<CustomResponseBody<Void>> deleteTimeOff(
            @PathVariable Long businessId,
            @PathVariable Long staffId,
            @PathVariable Long timeOffId) {

        Long ownerId = SecurityUtils.getCurrentUserId();
        timeOffService.deleteTimeOff(businessId, staffId, timeOffId, ownerId);
        return ResponseEntity.ok(CustomResponseBody.ok(null, "Time off deleted successfully"));
    }

    @GetMapping("/{timeOffId}")
    public ResponseEntity<CustomResponseBody<TimeOffResponse>> getTimeOffById(
            @PathVariable Long businessId,
            @PathVariable Long staffId,
            @PathVariable Long timeOffId) {

        TimeOffResponse response = timeOffService.getTimeOffById(businessId, staffId, timeOffId);
        return ResponseEntity.ok(CustomResponseBody.ok(response, "Time off details retrieved successfully"));
    }

    @GetMapping
    public ResponseEntity<CustomResponseBody<List<TimeOffResponse>>> getTimeOffsInRange(
            @PathVariable Long businessId,
            @PathVariable Long staffId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startTime,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endTime) {

        List<TimeOffResponse> responses;
        if (startTime != null && endTime != null) {
            responses = timeOffService.getStaffTimeOffsInDateRange(businessId, staffId, startTime, endTime);
        } else {
            responses = timeOffService.getAllTimeOffsOfStaff(businessId, staffId);
        }
        return ResponseEntity.ok(CustomResponseBody.ok(responses, "Time offs retrieved successfully"));
    }
}
