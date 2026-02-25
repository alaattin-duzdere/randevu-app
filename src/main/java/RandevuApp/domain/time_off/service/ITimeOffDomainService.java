package RandevuApp.domain.time_off.service;

import RandevuApp.domain.staff.model.Staff;
import RandevuApp.domain.time_off.model.TimeOff;
import RandevuApp.domain.time_off.model.TimeOffType;

import java.time.LocalDateTime;
import java.util.List;

public interface ITimeOffDomainService {

    // --- FACTORY ---
    TimeOff createEntity(Staff staff, LocalDateTime startTime, LocalDateTime endTime, TimeOffType type, String note);

    // --- BUSINESS RULES ---
    void validateDates(LocalDateTime startTime, LocalDateTime endTime);

    void validateNoOverlap(Long staffId, LocalDateTime startTime, LocalDateTime endTime, Long excludeTimeOffId);

    // --- (REPOSITORY WRAPPERS) ---

    TimeOff save(TimeOff timeOff);

    TimeOff getByIdAndStaffId(Long timeOffId, Long staffId);

    List<TimeOff> getAllByStaffId(Long staffId);

    List<TimeOff> getStaffTimeOffsBetween(Long staffId, LocalDateTime startDate, LocalDateTime endDate);

    void delete(TimeOff timeOff);
}