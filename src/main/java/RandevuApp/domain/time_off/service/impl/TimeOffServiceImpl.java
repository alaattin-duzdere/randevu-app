package RandevuApp.domain.time_off.service.impl;

import RandevuApp.domain.business.model.Business;
import RandevuApp.domain.business.service.IBusinessDomainService;
import RandevuApp.domain.time_off.dto.CreateTimeOffRequest;
import RandevuApp.domain.time_off.dto.TimeOffResponse;
import RandevuApp.domain.time_off.dto.UpdateTimeOffRequest;
import RandevuApp.domain.staff.model.Staff;
import RandevuApp.domain.time_off.mapper.TimeOffMapper;
import RandevuApp.domain.time_off.model.TimeOff;
import RandevuApp.domain.staff.service.IStaffDomainService;
import RandevuApp.domain.time_off.service.ITimeOffDomainService;
import RandevuApp.domain.time_off.service.ITimeOffService;
import RandevuApp.domain.time_off.service.params.CreateTimeOffParams;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TimeOffServiceImpl implements ITimeOffService {

    private final ITimeOffDomainService timeOffDomainService;
    private final IBusinessDomainService businessDomainService;
    private final IStaffDomainService staffDomainService;
    private final TimeOffMapper mapper;

    @Override
    @Transactional
    public TimeOffResponse createTimeOff(Long businessId, Long staffId, CreateTimeOffRequest request, Long ownerId) {
        Staff staff = getStaffAndValidateAccess(businessId, staffId, ownerId);

        timeOffDomainService.validateDates(request.startTime(), request.endTime());

        timeOffDomainService.validateNoOverlap(staffId, request.startTime(), request.endTime(), null);

        CreateTimeOffParams params = new CreateTimeOffParams(
                staff,
                request.startTime(),
                request.endTime(),
                request.type(),
                request.note()
        );
        TimeOff timeOff = timeOffDomainService.createEntity(params);

        TimeOff savedTimeOff = timeOffDomainService.save(timeOff);
        return mapper.entityToResponse(savedTimeOff);
    }

    @Override
    @Transactional
    public TimeOffResponse updateTimeOff(Long businessId, Long staffId, Long timeOffId, UpdateTimeOffRequest request, Long ownerId) {
        getStaffAndValidateAccess(businessId, staffId, ownerId);

        TimeOff timeOff = timeOffDomainService.getByIdAndStaffId(timeOffId, staffId);

        mapper.updateTimeOffFromDto(request, timeOff);

        timeOffDomainService.validateDates(timeOff.getStartTime(), timeOff.getEndTime());

        timeOffDomainService.validateNoOverlap(staffId, timeOff.getStartTime(), timeOff.getEndTime(), timeOffId);

        TimeOff updatedTimeOff = timeOffDomainService.save(timeOff);
        return mapper.entityToResponse(updatedTimeOff);
    }

    @Override
    @Transactional
    public void deleteTimeOff(Long businessId, Long staffId, Long timeOffId, Long ownerId) {
        getStaffAndValidateAccess(businessId, staffId, ownerId);
        TimeOff timeOff = timeOffDomainService.getByIdAndStaffId(timeOffId, staffId);
        timeOffDomainService.delete(timeOff);
    }

    @Override
    @Transactional(readOnly = true)
    public List<TimeOffResponse> getAllTimeOffsOfStaff(Long businessId, Long staffId) {
        staffDomainService.getByIdAndBusinessId(staffId, businessId);

        List<TimeOff> timeOffs = timeOffDomainService.getAllByStaffId(staffId);
        return timeOffs.stream().map(mapper::entityToResponse).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<TimeOffResponse> getStaffTimeOffsInDateRange(Long businessId, Long staffId, LocalDateTime startDate, LocalDateTime endDate) {
        staffDomainService.getByIdAndBusinessId(staffId, businessId);

        List<TimeOff> timeOffs = timeOffDomainService.getStaffTimeOffsBetween(staffId, startDate, endDate);
        return timeOffs.stream().map(mapper::entityToResponse).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public TimeOffResponse getTimeOffById(Long businessId, Long staffId, Long timeOffId) {
        staffDomainService.getByIdAndBusinessId(staffId, businessId);
        TimeOff timeOff = timeOffDomainService.getByIdAndStaffId(timeOffId, staffId);
        return mapper.entityToResponse(timeOff);
    }

    // --- HELPER METHOD ---
    private Staff getStaffAndValidateAccess(Long businessId, Long staffId, Long ownerId) {
        Business business = businessDomainService.getById(businessId);

        businessDomainService.validateBusinessOwner(business, ownerId);
        return staffDomainService.getByIdAndBusinessId(staffId, businessId);
    }
}