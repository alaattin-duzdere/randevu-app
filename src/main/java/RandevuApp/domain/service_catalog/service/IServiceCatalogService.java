package RandevuApp.domain.service_catalog.service;

import RandevuApp.domain.service_catalog.dto.CreateServiceCatalogRequest;
import RandevuApp.domain.service_catalog.dto.ServiceCatalogResponse;
import RandevuApp.domain.service_catalog.dto.UpdateServiceCatalogRequest;

import java.util.List;

public interface IServiceCatalogService {
    ServiceCatalogResponse createCatalog(CreateServiceCatalogRequest request);
    ServiceCatalogResponse updateCatalog(Long catalogId, UpdateServiceCatalogRequest request);
    void deleteCatalog(Long catalogId);
    List<ServiceCatalogResponse> getAllCatalogs();
}
