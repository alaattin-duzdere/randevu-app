package RandevuApp.domain.business.repository;

import RandevuApp.domain.business.model.BusinessScheduleOverride;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface BusinessScheduleOverrideRepository extends JpaRepository<BusinessScheduleOverride, Long> {

    Optional<BusinessScheduleOverride> findByIdAndBusinessId(Long id, Long businessId);

    List<BusinessScheduleOverride> findAllByBusinessIdAndTargetDateBetween(Long businessId, LocalDate startDate, LocalDate endDate);

    Optional<BusinessScheduleOverride> findByBusinessIdAndTargetDate(Long businessId, LocalDate targetDate);
}