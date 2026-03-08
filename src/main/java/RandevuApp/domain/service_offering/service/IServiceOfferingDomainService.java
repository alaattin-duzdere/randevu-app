package RandevuApp.domain.service_offering.service;

import RandevuApp.domain.business.model.Business;
import RandevuApp.domain.service_catalog.model.ServiceCatalog;
import RandevuApp.domain.service_offering.model.ServiceOffering;
import RandevuApp.domain.service_offering.service.param.CreateServiceOfferingParams;
import RandevuApp.domain.service_offering.service.param.UpdateServiceOfferingParams;

import java.math.BigDecimal;
import java.util.List;

public interface IServiceOfferingDomainService {

    // --- ENTITY CREATION  ---
    ServiceOffering createEntity(CreateServiceOfferingParams params,Business business);

    ServiceOffering cloneFromCatalog(ServiceCatalog catalog, Business business);

    // --- BUSINESS RULES ---
    void validateServiceNameIsUniqueForBusiness(Long businessId, String serviceName);

    ServiceOffering performUpdate(ServiceOffering serviceOffering, UpdateServiceOfferingParams params);


    // --- REPOSITORY WRAPPERS ---

    ServiceOffering save(ServiceOffering serviceOffering);

    List<ServiceOffering> saveAll(List<ServiceOffering> serviceOfferings);

    ServiceOffering getById(Long id);

    ServiceOffering getByIdAndBusinessId(Long serviceId, Long businessId);

    List<ServiceOffering> getAllByBusinessId(Long businessId);

    void delete(ServiceOffering serviceOffering);

    void deleteAllByBusinessId(Long businessId);
}
