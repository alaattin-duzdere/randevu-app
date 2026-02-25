package RandevuApp.domain.business.service.impl;

import RandevuApp.domain.business.dto.OperatingHourDto;
import RandevuApp.domain.business.dto.ScheduleOverrideRequest;
import RandevuApp.domain.business.dto.ScheduleOverrideResponse;
import RandevuApp.domain.business.mapper.BusinessScheduleMapper;
import RandevuApp.domain.business.model.Business;
import RandevuApp.domain.business.model.BusinessOperatingHour;
import RandevuApp.domain.business.model.BusinessScheduleOverride;
import RandevuApp.domain.business.service.IBusinessDomainService;
import RandevuApp.domain.business.service.IBusinessScheduleDomainService;
import RandevuApp.domain.business.service.IBusinessScheduleService;
import RandevuApp.exceptions.client.InvalidInputException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BusinessScheduleServiceImpl implements IBusinessScheduleService {

    private final IBusinessScheduleDomainService scheduleDomainService;
    private final IBusinessDomainService businessDomainService;
    private final BusinessScheduleMapper mapper;

    // --- STANDARD  ---

    @Override
    @Transactional(readOnly = true)
    public List<OperatingHourDto> getOperatingHours(Long businessId) {
        businessDomainService.getById(businessId);

        List<BusinessOperatingHour> operatingHours = scheduleDomainService.getOperatingHoursByBusinessId(businessId);
        return operatingHours.stream().map(mapper::toOperatingHourDto).toList();
    }

    @Override
    @Transactional
    public List<OperatingHourDto> updateOperatingHours(Long businessId, List<OperatingHourDto> requests, Long ownerId) {
        Business business = businessDomainService.getById(businessId);
        businessDomainService.validateBusinessOwner(business, ownerId);

        if (requests == null || requests.size() != 7) {
            throw new InvalidInputException("Operating hours must be provided for exactly 7 days.");
        }

        List<BusinessOperatingHour> existingHours = scheduleDomainService.getOperatingHoursByBusinessId(businessId);

        for (BusinessOperatingHour existingHour : existingHours) {

            OperatingHourDto matchingRequest = requests.stream()
                    .filter(req -> req.getDayOfWeek() == existingHour.getDayOfWeek())
                    .findFirst()
                    .orElseThrow(() -> new InvalidInputException("Missing data for day: " + existingHour.getDayOfWeek()));

            existingHour.setClosed(matchingRequest.getIsClosed());
            existingHour.setOpenTime(matchingRequest.getOpenTime());
            existingHour.setCloseTime(matchingRequest.getCloseTime());

            scheduleDomainService.validateTimes(existingHour.isClosed(), existingHour.getOpenTime(), existingHour.getCloseTime());
        }


        return existingHours.stream().map(mapper::toOperatingHourDto).toList();
    }


    // --- OVERRIDE OPS ---

    @Override
    @Transactional
    public ScheduleOverrideResponse createOverride(Long businessId, ScheduleOverrideRequest request, Long ownerId) {
        Business business = businessDomainService.getById(businessId);
        businessDomainService.validateBusinessOwner(business, ownerId);

        scheduleDomainService.validateTimes(request.getIsClosed(), request.getOpenTime(), request.getCloseTime());

        BusinessScheduleOverride newOverride = new BusinessScheduleOverride();
        newOverride.setBusiness(business);
        newOverride.setTargetDate(request.getTargetDate());
        newOverride.setClosed(request.getIsClosed());
        newOverride.setOpenTime(request.getOpenTime());
        newOverride.setCloseTime(request.getCloseTime());
        newOverride.setReason(request.getReason());

        BusinessScheduleOverride savedOverride = scheduleDomainService.saveOverride(newOverride);
        return mapper.toOverrideResponse(savedOverride);
    }

    @Override
    @Transactional
    public ScheduleOverrideResponse updateOverride(Long businessId, Long overrideId, ScheduleOverrideRequest request, Long ownerId) {
        Business business = businessDomainService.getById(businessId);
        businessDomainService.validateBusinessOwner(business, ownerId);

        BusinessScheduleOverride existingOverride = scheduleDomainService.getOverrideByIdAndBusinessId(overrideId, businessId);

        mapper.updateOverrideFromDto(request, existingOverride);

        scheduleDomainService.validateTimes(existingOverride.isClosed(), existingOverride.getOpenTime(), existingOverride.getCloseTime());

        BusinessScheduleOverride updatedOverride = scheduleDomainService.saveOverride(existingOverride);
        return mapper.toOverrideResponse(updatedOverride);
    }

    @Override
    @Transactional
    public void deleteOverride(Long businessId, Long overrideId, Long ownerId) {
        Business business = businessDomainService.getById(businessId);
        businessDomainService.validateBusinessOwner(business, ownerId);
        BusinessScheduleOverride override = scheduleDomainService.getOverrideByIdAndBusinessId(overrideId, businessId);
        scheduleDomainService.deleteOverride(override);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ScheduleOverrideResponse> getOverridesInDateRange(Long businessId, LocalDate startDate, LocalDate endDate) {
        businessDomainService.getById(businessId);
        List<BusinessScheduleOverride> overrides = scheduleDomainService.getOverridesByBusinessIdAndDateRange(businessId, startDate, endDate);
        return overrides.stream().map(mapper::toOverrideResponse).toList();
    }
}
