package RandevuApp.domain.service_offering.service;

import RandevuApp.domain.service_offering.dto.CreateServiceOfferingRequest;
import RandevuApp.domain.service_offering.dto.ServiceOfferingResponse;
import RandevuApp.domain.service_offering.dto.UpdateServiceOfferingRequest;

import java.util.List;

public interface IServiceOfferingService {

    // Custom Services
    ServiceOfferingResponse createCustomService(Long businessId, CreateServiceOfferingRequest request, Long ownerId);

    // Template Services
    List<ServiceOfferingResponse> getSystemReadyServices();
    List<ServiceOfferingResponse> addServicesFromCatalog(Long businessId, List<Long> catalogIds, Long ownerId);

    // Standard Crud ops
    ServiceOfferingResponse updateService(Long businessId, Long serviceId, UpdateServiceOfferingRequest request, Long ownerId);
    void deleteService(Long businessId, Long serviceId, Long ownerId);
    List<ServiceOfferingResponse> getAllServicesOfBusiness(Long businessId);
    ServiceOfferingResponse getServiceById(Long businessId, Long serviceId);

}
