package RandevuApp.domain.business.repository;

import RandevuApp.domain.business.model.BusinessScheduleOverride;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;

@Repository
public interface BusinessScheduleOverrideRepository extends JpaRepository<BusinessScheduleOverride, Long>, JpaSpecificationExecutor<BusinessScheduleOverride> {

    Optional<BusinessScheduleOverride> findByIdAndBusinessId(Long id, Long businessId);

    Optional<BusinessScheduleOverride> findByBusinessIdAndTargetDate(Long businessId, LocalDate targetDate);
}