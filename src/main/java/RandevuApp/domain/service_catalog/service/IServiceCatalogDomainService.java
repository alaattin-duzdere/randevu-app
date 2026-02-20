package RandevuApp.domain.service_catalog.service;

import RandevuApp.domain.service_catalog.dto.UpdateServiceCatalogRequest;
import RandevuApp.domain.service_catalog.model.ServiceCatalog;

import java.util.List;

public interface IServiceCatalogDomainService {
    ServiceCatalog createEntity(String name, String description, Integer duration, java.math.BigDecimal price);
    ServiceCatalog performUpdate(ServiceCatalog catalog, UpdateServiceCatalogRequest request);
    void validateCatalogNameIsUnique(String name);

    ServiceCatalog save(ServiceCatalog catalog);
    ServiceCatalog getById(Long id);
    List<ServiceCatalog> getAllActiveCatalogs();
    void delete(ServiceCatalog catalog);
}
