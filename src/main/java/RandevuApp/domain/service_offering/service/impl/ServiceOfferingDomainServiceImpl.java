package RandevuApp.domain.service_offering.service.impl;

import RandevuApp.domain.business.model.Business;
import RandevuApp.domain.service_catalog.model.ServiceCatalog;
import RandevuApp.domain.service_offering.dto.UpdateServiceOfferingRequest;
import RandevuApp.domain.service_offering.model.ServiceOffering;
import RandevuApp.domain.service_offering.repository.ServiceOfferingRepository;
import RandevuApp.domain.service_offering.service.IServiceOfferingDomainService;
import RandevuApp.exceptions.client.ConflictException;
import RandevuApp.exceptions.client.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ServiceOfferingDomainServiceImpl implements IServiceOfferingDomainService {

    private final ServiceOfferingRepository serviceOfferingRepository;

    // Factory Methods

    @Override
    public ServiceOffering createEntity(String name, String description, Integer duration, BigDecimal price, Business business) {
        return ServiceOffering.builder()
                .name(name)
                .description(description)
                .durationInMinutes(duration)
                .price(price)
                .active(true)
                .business(business)
                .build();
    }

    public ServiceOffering cloneFromCatalog(ServiceCatalog catalog, Business business) {
        return ServiceOffering.builder()
                .name(catalog.getName())
                .description(catalog.getDescription())
                .durationInMinutes(catalog.getDurationInMinutes())
                .price(catalog.getPrice())
                .active(true)
                .business(business)
                .build();
    }

    // Business RULES

    @Override
    public void validateServiceNameIsUniqueForBusiness(Long businessId, String serviceName) {
        boolean exists = serviceOfferingRepository.existsByBusinessIdAndNameIgnoreCase(businessId, serviceName);
        if (exists) {
            throw new ConflictException("Bu işletmede '" + serviceName + "' adında bir hizmet zaten mevcut.");
        }
    }

    @Override
    public ServiceOffering performUpdate(ServiceOffering serviceOffering, UpdateServiceOfferingRequest request) {
        if (request.getName() != null && !request.getName().isBlank()) {
            serviceOffering.setName(request.getName());
        }
        if (request.getDescription() != null) {
            serviceOffering.setDescription(request.getDescription());
        }
        if (request.getDurationInMinutes() != null) {
            serviceOffering.setDurationInMinutes(request.getDurationInMinutes());
        }
        if (request.getPrice() != null) {
            serviceOffering.setPrice(request.getPrice());
        }
        if (request.getActive() != null) {
            serviceOffering.setActive(request.getActive());
        }

        return serviceOffering;
    }

    // DB Wrappers

    @Override
    public ServiceOffering save(ServiceOffering serviceOffering) {
        return serviceOfferingRepository.save(serviceOffering);
    }

    @Override
    public List<ServiceOffering> saveAll(List<ServiceOffering> serviceOfferings) {
        return serviceOfferingRepository.saveAll(serviceOfferings);
    }

    @Override
    public ServiceOffering getById(Long id) {
        return serviceOfferingRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Hizmet bulunamadı", "id", id));
    }

    @Override
    public ServiceOffering getByIdAndBusinessId(Long serviceId, Long businessId) {
        return serviceOfferingRepository.findByIdAndBusinessId(serviceId, businessId)
                .orElseThrow(() -> new ResourceNotFoundException("Bu işletmeye ait hizmet bulunamadı", "id", serviceId));
    }

    @Override
    public List<ServiceOffering> getAllByBusinessId(Long businessId) {
        return serviceOfferingRepository.findAllByBusinessId(businessId);
    }

    @Override
    public void delete(ServiceOffering serviceOffering) {
        serviceOfferingRepository.delete(serviceOffering);
    }

    @Override
    public void deleteAllByBusinessId(Long businessId) {
        serviceOfferingRepository.deleteAllByBusinessId(businessId);
    }

}
