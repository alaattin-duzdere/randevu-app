package RandevuApp.domain.service_catalog.service;

import RandevuApp.domain.service_catalog.model.ServiceCatalog;
import RandevuApp.domain.service_catalog.service.param.CreateServiceCatalogParams;
import RandevuApp.domain.service_catalog.service.param.UpdateServiceCatalogParams;

import java.math.BigDecimal;
import java.util.List;

public interface IServiceCatalogDomainService {
    ServiceCatalog createEntity(CreateServiceCatalogParams params);
    ServiceCatalog performUpdate(ServiceCatalog catalog, UpdateServiceCatalogParams params);
    void validateCatalogNameIsUnique(String name);

    ServiceCatalog save(ServiceCatalog catalog);
    ServiceCatalog getById(Long id);
    List<ServiceCatalog> getAllActiveCatalogs();
    void delete(ServiceCatalog catalog);
}
