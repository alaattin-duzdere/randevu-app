package RandevuApp.domain.time_off.controller;

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
    @ResponseStatus(HttpStatus.CREATED)
    public TimeOffResponse createTimeOff(
            @PathVariable Long businessId,
            @PathVariable Long staffId,
            @Valid @RequestBody CreateTimeOffRequest request) {
        
        Long ownerId = SecurityUtils.getCurrentUserId();
        return timeOffService.createTimeOff(businessId, staffId, request, ownerId);
    }

    @PutMapping("/{timeOffId}")
    public TimeOffResponse updateTimeOff(
            @PathVariable Long businessId,
            @PathVariable Long staffId,
            @PathVariable Long timeOffId,
            @Valid @RequestBody UpdateTimeOffRequest request) {

        Long ownerId = SecurityUtils.getCurrentUserId();
        return timeOffService.updateTimeOff(businessId, staffId, timeOffId, request, ownerId);
    }

    @DeleteMapping("/{timeOffId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteTimeOff(
            @PathVariable Long businessId,
            @PathVariable Long staffId,
            @PathVariable Long timeOffId) {

        Long ownerId = SecurityUtils.getCurrentUserId();
        timeOffService.deleteTimeOff(businessId, staffId, timeOffId, ownerId);
    }

    @GetMapping("/{timeOffId}")
    public TimeOffResponse getTimeOffById(
            @PathVariable Long businessId,
            @PathVariable Long staffId,
            @PathVariable Long timeOffId) {

        return timeOffService.getTimeOffById(businessId, staffId, timeOffId);
    }

    @GetMapping
    public List<TimeOffResponse> getTimeOffsInRange(
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
        return responses;
    }
}
