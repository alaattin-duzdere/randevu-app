package RandevuApp.domain.business.service;

import RandevuApp.domain.business.model.Business;
import RandevuApp.domain.business.model.BusinessOperatingHour;
import RandevuApp.domain.business.model.BusinessScheduleOverride;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

public interface IBusinessScheduleDomainService {

    void validateTimes(boolean isClosed, LocalTime openTime, LocalTime closeTime);

    // --- OPERATING HOURS ---
    List<BusinessOperatingHour> createDefaultOperatingHoursForBusiness(Business business);

    List<BusinessOperatingHour> getOperatingHoursByBusinessId(Long businessId);

    // --- OVERRIDES ---

    BusinessScheduleOverride saveOverride(BusinessScheduleOverride override);

    void deleteOverride(BusinessScheduleOverride override);

    BusinessScheduleOverride getOverrideByIdAndBusinessId(Long overrideId, Long businessId);

    List<BusinessScheduleOverride> getOverridesByBusinessIdAndDateRange(Long businessId, LocalDate startDate, LocalDate endDate);

    Optional<BusinessOperatingHour> getEffectiveOperatingHours(Long businessId, LocalDate date);
}