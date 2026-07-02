package RandevuApp.domain.service_offering.repository;

import RandevuApp.domain.service_offering.model.ServiceOffering;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ServiceOfferingRepository extends JpaRepository<ServiceOffering, Long> {
    
    Optional<ServiceOffering> findByIdAndBusinessId(Long id, Long businessId);

    List<ServiceOffering> findAllByIdInAndBusinessId(List<Long> ids, Long businessId);
    
    List<ServiceOffering> findAllByBusinessId(Long businessId);
    
    boolean existsByBusinessIdAndNameIgnoreCase(Long businessId, String name);

    void deleteAllByBusinessId(Long businessId);
}
