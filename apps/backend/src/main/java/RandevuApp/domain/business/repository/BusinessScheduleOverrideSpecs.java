
package RandevuApp.domain.business.repository;

import RandevuApp.domain.business.model.BusinessScheduleOverride;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;

public class BusinessScheduleOverrideSpecs {

    private BusinessScheduleOverrideSpecs() {
    }

    public static Specification<BusinessScheduleOverride> hasBusinessId(Long businessId) {
        return (root, query, cb) -> cb.equal(root.get("business").get("id"), businessId);
    }

    public static Specification<BusinessScheduleOverride> isAfterOrEqual(LocalDate startDate) {
        return (root, query, cb) -> cb.greaterThanOrEqualTo(root.get("targetDate"), startDate);
    }

    public static Specification<BusinessScheduleOverride> isBeforeOrEqual(LocalDate endDate) {
        return (root, query, cb) -> cb.lessThanOrEqualTo(root.get("targetDate"), endDate);
    }
}
