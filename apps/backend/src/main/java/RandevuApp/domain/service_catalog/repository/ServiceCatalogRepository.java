package RandevuApp.domain.service_catalog.repository;

import RandevuApp.domain.service_catalog.model.ServiceCatalog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ServiceCatalogRepository extends JpaRepository<ServiceCatalog, Long> {
    boolean existsByNameIgnoreCase(String name);

    List<ServiceCatalog> findAllByActiveTrue();
}
