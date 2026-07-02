package RandevuApp.domain.service_offering.service.impl;

import RandevuApp.domain.business.model.Business;
import RandevuApp.domain.business.service.IBusinessDomainService;
import RandevuApp.domain.service_catalog.model.ServiceCatalog;
import RandevuApp.domain.service_catalog.service.IServiceCatalogDomainService;
import RandevuApp.domain.service_offering.dto.CreateServiceOfferingRequest;
import RandevuApp.domain.service_offering.dto.ServiceOfferingResponse;
import RandevuApp.domain.service_offering.dto.UpdateServiceOfferingRequest;
import RandevuApp.domain.service_offering.mapper.ServiceOfferingMapper;
import RandevuApp.domain.service_offering.model.ServiceOffering;
import RandevuApp.domain.service_offering.service.param.CreateServiceOfferingParams;
import RandevuApp.domain.service_offering.service.param.UpdateServiceOfferingParams;
import RandevuApp.domain.service_offering.service.IServiceOfferingDomainService;
import RandevuApp.domain.service_offering.service.IServiceOfferingService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ServiceOfferingServiceImpl implements IServiceOfferingService {

    private final IBusinessDomainService businessDomainService;
    private final IServiceOfferingDomainService serviceOfferingDomainService;
    private final ServiceOfferingMapper mapper;
    private final IServiceCatalogDomainService catalogDomainService;

    @Override
    @Transactional
    public ServiceOfferingResponse createCustomService(Long businessId, CreateServiceOfferingRequest request, Long ownerId) {
        // find and validate business
        Business business = getBusinessAndValidateOwner(businessId, ownerId);

        // validate service name
        serviceOfferingDomainService.validateServiceNameIsUniqueForBusiness(businessId, request.name());

        // create entity
        CreateServiceOfferingParams params = new CreateServiceOfferingParams(
                request.name(),
                request.description(),
                request.durationInMinutes(),
                request.price(),
                business
        );
        ServiceOffering serviceOffering = serviceOfferingDomainService.createEntity(params, business);

        // save and return
        ServiceOffering savedService = serviceOfferingDomainService.save(serviceOffering);
        return mapper.entityToResponse(savedService);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ServiceOfferingResponse> getSystemReadyServices() {
        List<ServiceCatalog> activeCatalogs = catalogDomainService.getAllActiveCatalogs();

        return activeCatalogs.stream()
                .map(catalog -> ServiceOfferingResponse.builder()
                        .id(catalog.getId())
                        .businessId(null)
                        .name(catalog.getName())
                        .description(catalog.getDescription())
                        .durationInMinutes(catalog.getDurationInMinutes())
                        .price(catalog.getPrice())
                        .active(catalog.isActive())
                        .build())
                .toList();
    }

    @Override
    public List<ServiceOfferingResponse> addServicesFromCatalog(Long businessId, List<Long> catalogIds, Long ownerId) {
        Business business = getBusinessAndValidateOwner(businessId, ownerId);

        List<ServiceOffering> newServices = new ArrayList<>();

        for (Long catalogId : catalogIds) {
            // get catalog service
            ServiceCatalog catalog = catalogDomainService.getById(catalogId);

            // validate if exist with name ( maybe throw exc )
            serviceOfferingDomainService.validateServiceNameIsUniqueForBusiness(businessId, catalog.getName());

            // clone from catalog
            ServiceOffering clonedService = serviceOfferingDomainService.cloneFromCatalog(catalog, business);
            newServices.add(clonedService);
        }

        // save list and return
        List<ServiceOffering> savedServices = serviceOfferingDomainService.saveAll(newServices);
        return savedServices.stream().map(mapper::entityToResponse).toList();
    }

    @Override
    @Transactional
    public ServiceOfferingResponse updateService(Long businessId, Long serviceId, UpdateServiceOfferingRequest request, Long ownerId) {
        getBusinessAndValidateOwner(businessId, ownerId);

        ServiceOffering serviceOffering = serviceOfferingDomainService.getByIdAndBusinessId(serviceId, businessId);

        // unique check for service name change
        if (request.name() != null && !request.name().equals(serviceOffering.getName())) {
            serviceOfferingDomainService.validateServiceNameIsUniqueForBusiness(businessId, request.name());
        }

        UpdateServiceOfferingParams params = new UpdateServiceOfferingParams(
                request.name(),
                request.description(),
                request.durationInMinutes(),
                request.price(),
                request.active()
        );

        ServiceOffering updatedService = serviceOfferingDomainService.performUpdate(serviceOffering, params);

        serviceOfferingDomainService.save(updatedService);

        return mapper.entityToResponse(updatedService);
    }

    @Override
    @Transactional
    public void deleteService(Long businessId, Long serviceId, Long ownerId) {
        getBusinessAndValidateOwner(businessId, ownerId);

        ServiceOffering serviceOffering = serviceOfferingDomainService.getByIdAndBusinessId(serviceId, businessId);

        /* boolean hasActiveAppointments = appointmentRepository.existsByServiceIdAndStatusIn(...);
        if(hasActiveAppointments) throw exception;
        */

        serviceOfferingDomainService.delete(serviceOffering);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ServiceOfferingResponse> getAllServicesOfBusiness(Long businessId) {
        businessDomainService.getById(businessId);

        List<ServiceOffering> services = serviceOfferingDomainService.getAllByBusinessId(businessId);
        return services.stream().map(mapper::entityToResponse).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public ServiceOfferingResponse getServiceById(Long businessId, Long serviceId) {
        businessDomainService.getById(businessId);
        ServiceOffering service = serviceOfferingDomainService.getByIdAndBusinessId(serviceId, businessId);
        return mapper.entityToResponse(service);
    }

    // --- HELPER METHOD ---
    private Business getBusinessAndValidateOwner(Long businessId, Long ownerId) {
        Business business = businessDomainService.getById(businessId);
        businessDomainService.validateBusinessOwner(business, ownerId);
        return business;
    }
}
