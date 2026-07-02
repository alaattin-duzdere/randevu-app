package RandevuApp.domain.time_off.service;

import RandevuApp.domain.time_off.dto.CreateTimeOffRequest;
import RandevuApp.domain.time_off.dto.TimeOffResponse;
import RandevuApp.domain.time_off.dto.UpdateTimeOffRequest;

import java.time.LocalDateTime;
import java.util.List;

public interface ITimeOffService {

    // --- STANDARD CRUD ---

    TimeOffResponse createTimeOff(Long businessId, Long staffId, CreateTimeOffRequest request, Long ownerId);

    TimeOffResponse updateTimeOff(Long businessId, Long staffId, Long timeOffId, UpdateTimeOffRequest request, Long ownerId);

    void deleteTimeOff(Long businessId, Long staffId, Long timeOffId, Long ownerId);

    // --- List ---
    List<TimeOffResponse> getAllTimeOffsOfStaff(Long businessId, Long staffId);
    
    List<TimeOffResponse> getStaffTimeOffsInDateRange(Long businessId, Long staffId, LocalDateTime startDate, LocalDateTime endDate);

    TimeOffResponse getTimeOffById(Long businessId, Long staffId, Long timeOffId);
}
