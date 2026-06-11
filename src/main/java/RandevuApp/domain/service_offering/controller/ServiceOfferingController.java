package RandevuApp.domain.service_offering.controller;

import RandevuApp.domain.service_offering.dto.CreateServiceOfferingRequest;
import RandevuApp.domain.service_offering.dto.ServiceOfferingResponse;
import RandevuApp.domain.service_offering.dto.UpdateServiceOfferingRequest;
import RandevuApp.domain.service_offering.service.IServiceOfferingService;
import RandevuApp.utils.SecurityUtils;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ServiceOfferingController {

    private final IServiceOfferingService serviceOfferingService;

    // --- SYSTEM TEMPLATES ---

    @GetMapping("/services/templates")
    public List<ServiceOfferingResponse> getSystemReadyServices() {
        return serviceOfferingService.getSystemReadyServices();
    }

    // --- BUSINESS SERVICES ---

    @PostMapping("/businesses/{businessId}/services")
    @ResponseStatus(HttpStatus.CREATED)
    public ServiceOfferingResponse createCustomService(
            @PathVariable Long businessId,
            @Valid @RequestBody CreateServiceOfferingRequest request) {
        
        Long ownerId = SecurityUtils.getCurrentUserId();
        return serviceOfferingService.createCustomService(businessId, request, ownerId);
    }

    @PostMapping("/businesses/{businessId}/services/from-catalog")
    public List<ServiceOfferingResponse> addServicesFromCatalog(
            @PathVariable Long businessId,
            @RequestBody List<Long> catalogIds) {
        
        Long ownerId = SecurityUtils.getCurrentUserId();
        return serviceOfferingService.addServicesFromCatalog(businessId, catalogIds, ownerId);
    }

    @GetMapping("/businesses/{businessId}/services")
    public List<ServiceOfferingResponse> getAllServicesOfBusiness(@PathVariable Long businessId) {
        return serviceOfferingService.getAllServicesOfBusiness(businessId);
    }

    @GetMapping("/businesses/{businessId}/services/{serviceId}")
    public ServiceOfferingResponse getServiceById(
            @PathVariable Long businessId,
            @PathVariable Long serviceId) {
        return serviceOfferingService.getServiceById(businessId, serviceId);
    }

    @PutMapping("/businesses/{businessId}/services/{serviceId}")
    public ServiceOfferingResponse updateService(
            @PathVariable Long businessId,
            @PathVariable Long serviceId,
            @Valid @RequestBody UpdateServiceOfferingRequest request) {
        
        Long ownerId = SecurityUtils.getCurrentUserId();
        return serviceOfferingService.updateService(businessId, serviceId, request, ownerId);
    }

    @DeleteMapping("/businesses/{businessId}/services/{serviceId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteService(
            @PathVariable Long businessId,
            @PathVariable Long serviceId) {
        
        Long ownerId = SecurityUtils.getCurrentUserId();
        serviceOfferingService.deleteService(businessId, serviceId, ownerId);
    }
}
