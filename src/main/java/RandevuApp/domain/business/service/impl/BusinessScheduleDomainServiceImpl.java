package RandevuApp.domain.business.service.impl;

import RandevuApp.domain.business.model.Business;
import RandevuApp.domain.business.model.BusinessOperatingHour;
import RandevuApp.domain.business.model.BusinessScheduleOverride;
import RandevuApp.domain.business.repository.BusinessOperatingHourRepository;
import RandevuApp.domain.business.repository.BusinessScheduleOverrideRepository;
import RandevuApp.domain.business.service.IBusinessScheduleDomainService;
import RandevuApp.exceptions.client.InvalidInputException;
import RandevuApp.exceptions.client.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BusinessScheduleDomainServiceImpl implements IBusinessScheduleDomainService {

    private final BusinessOperatingHourRepository operatingHourRepository;
    private final BusinessScheduleOverrideRepository overrideRepository;

    // --- VALIDATION ---

    public void validateTimes(boolean isClosed, LocalTime openTime, LocalTime closeTime) {
        if (!isClosed) {
            if (openTime == null || closeTime == null) {
                throw new InvalidInputException("Dükkan açık (isClosed=false) olarak işaretlendiğinde açılış ve kapanış saatleri boş bırakılamaz.");
            }
            if (openTime.isAfter(closeTime) || openTime.equals(closeTime)) {
                throw new InvalidInputException("Açılış saati, kapanış saatinden önce olmalıdır.");
            }
        }
    }

    // --- OPERATING HOURS ---

    @Override
    public List<BusinessOperatingHour> createDefaultOperatingHoursForBusiness(Business business) {
        List<BusinessOperatingHour> defaultHours = new ArrayList<>();

        for (DayOfWeek day : DayOfWeek.values()) {
            BusinessOperatingHour hour = new BusinessOperatingHour();
            hour.setBusiness(business);
            hour.setDayOfWeek(day);

            if (day == DayOfWeek.SATURDAY || day == DayOfWeek.SUNDAY) {
                hour.setClosed(true);
            } else {
                hour.setClosed(false);
                hour.setOpenTime(LocalTime.of(9, 0));
                hour.setCloseTime(LocalTime.of(18, 0));
            }
            defaultHours.add(hour);
        }
        return operatingHourRepository.saveAll(defaultHours);
    }

    @Override
    public List<BusinessOperatingHour> getOperatingHoursByBusinessId(Long businessId) {
        return operatingHourRepository.findAllByBusinessId(businessId);
    }

    // --- OVERRIDES ---

    @Override
    public BusinessScheduleOverride saveOverride(BusinessScheduleOverride override) {
        return overrideRepository.save(override);
    }

    @Override
    public void deleteOverride(BusinessScheduleOverride override) {
        overrideRepository.delete(override);
    }

    @Override
    public BusinessScheduleOverride getOverrideByIdAndBusinessId(Long overrideId, Long businessId) {
        return overrideRepository.findByIdAndBusinessId(overrideId, businessId)
                .orElseThrow(() -> new ResourceNotFoundException("Belirtilen istisna kaydı bulunamadı.", "id", overrideId));
    }

    @Override
    public List<BusinessScheduleOverride> getOverridesByBusinessIdAndDateRange(Long businessId, LocalDate startDate, LocalDate endDate) {
        return overrideRepository.findAllByBusinessIdAndTargetDateBetween(businessId, startDate, endDate);
    }


    // for appointment booking
    @Override
    public Optional<BusinessOperatingHour> getEffectiveOperatingHours(Long businessId, LocalDate date) {
        Optional<BusinessScheduleOverride> overrideOpt = overrideRepository.findByBusinessIdAndTargetDate(businessId, date);

        if (overrideOpt.isPresent()) {
            BusinessScheduleOverride override = overrideOpt.get();

            BusinessOperatingHour effectiveHour = new BusinessOperatingHour();
            effectiveHour.setBusiness(override.getBusiness());
            effectiveHour.setDayOfWeek(date.getDayOfWeek());
            effectiveHour.setClosed(override.isClosed());
            effectiveHour.setOpenTime(override.getOpenTime());
            effectiveHour.setCloseTime(override.getCloseTime());

            return Optional.of(effectiveHour);
        }

        return operatingHourRepository.findByBusinessIdAndDayOfWeek(businessId, date.getDayOfWeek());
    }
}
