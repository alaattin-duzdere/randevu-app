package RandevuApp.domain.service_offering.controller;

import RandevuApp.api.CustomResponseBody;
import RandevuApp.domain.service_offering.dto.CreateServiceOfferingRequest;
import RandevuApp.domain.service_offering.dto.ServiceOfferingResponse;
import RandevuApp.domain.service_offering.dto.UpdateServiceOfferingRequest;
import RandevuApp.domain.service_offering.service.IServiceOfferingService;
import RandevuApp.utils.SecurityUtils;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ServiceOfferingController {

    private final IServiceOfferingService serviceOfferingService;

    // --- SYSTEM TEMPLATES ---

    @GetMapping("/services/templates")
    public ResponseEntity<CustomResponseBody<List<ServiceOfferingResponse>>> getSystemReadyServices() {
        List<ServiceOfferingResponse> services = serviceOfferingService.getSystemReadyServices();
        return ResponseEntity.ok(CustomResponseBody.ok(services, "System templates retrieved successfully"));
    }

    // --- BUSINESS SERVICES ---

    @PostMapping("/businesses/{businessId}/services")
    public ResponseEntity<CustomResponseBody<ServiceOfferingResponse>> createCustomService(
            @PathVariable Long businessId,
            @Valid @RequestBody CreateServiceOfferingRequest request) {
        
        Long ownerId = SecurityUtils.getCurrentUserId();
        ServiceOfferingResponse response = serviceOfferingService.createCustomService(businessId, request, ownerId);
        return ResponseEntity.status(HttpStatus.CREATED).body(CustomResponseBody.ok(response, "Service created successfully"));
    }

    @PostMapping("/businesses/{businessId}/services/from-catalog")
    public ResponseEntity<CustomResponseBody<List<ServiceOfferingResponse>>> addServicesFromCatalog(
            @PathVariable Long businessId,
            @RequestBody List<Long> catalogIds) {
        
        Long ownerId = SecurityUtils.getCurrentUserId();
        List<ServiceOfferingResponse> responses = serviceOfferingService.addServicesFromCatalog(businessId, catalogIds, ownerId);
        return ResponseEntity.status(HttpStatus.CREATED).body(CustomResponseBody.ok(responses, "Services added from catalog successfully"));
    }

    @GetMapping("/businesses/{businessId}/services")
    public ResponseEntity<CustomResponseBody<List<ServiceOfferingResponse>>> getAllServicesOfBusiness(@PathVariable Long businessId) {
        List<ServiceOfferingResponse> services = serviceOfferingService.getAllServicesOfBusiness(businessId);
        return ResponseEntity.ok(CustomResponseBody.ok(services, "Services retrieved successfully"));
    }

    @GetMapping("/businesses/{businessId}/services/{serviceId}")
    public ResponseEntity<CustomResponseBody<ServiceOfferingResponse>> getServiceById(
            @PathVariable Long businessId,
            @PathVariable Long serviceId) {
        ServiceOfferingResponse service = serviceOfferingService.getServiceById(businessId, serviceId);
        return ResponseEntity.ok(CustomResponseBody.ok(service, "Service retrieved successfully"));
    }

    @PutMapping("/businesses/{businessId}/services/{serviceId}")
    public ResponseEntity<CustomResponseBody<ServiceOfferingResponse>> updateService(
            @PathVariable Long businessId,
            @PathVariable Long serviceId,
            @Valid @RequestBody UpdateServiceOfferingRequest request) {
        
        Long ownerId = SecurityUtils.getCurrentUserId();
        ServiceOfferingResponse response = serviceOfferingService.updateService(businessId, serviceId, request, ownerId);
        return ResponseEntity.ok(CustomResponseBody.ok(response, "Service updated successfully"));
    }

    @DeleteMapping("/businesses/{businessId}/services/{serviceId}")
    public ResponseEntity<CustomResponseBody<Void>> deleteService(
            @PathVariable Long businessId,
            @PathVariable Long serviceId) {
        
        Long ownerId = SecurityUtils.getCurrentUserId();
        serviceOfferingService.deleteService(businessId, serviceId, ownerId);
        return ResponseEntity.ok(CustomResponseBody.ok(null, "Service deleted successfully"));
    }
}
