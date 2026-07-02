package RandevuApp.domain.business.repository;

import RandevuApp.domain.business.model.BusinessOperatingHour;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.DayOfWeek;
import java.util.List;
import java.util.Optional;

@Repository
public interface BusinessOperatingHourRepository extends JpaRepository<BusinessOperatingHour, Long> {

    Optional<BusinessOperatingHour> findByBusinessIdAndDayOfWeek(Long businessId, DayOfWeek dayOfWeek);

    List<BusinessOperatingHour> findAllByBusinessId(Long businessId);
}
