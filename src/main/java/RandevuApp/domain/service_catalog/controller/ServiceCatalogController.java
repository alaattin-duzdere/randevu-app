package RandevuApp.domain.service_catalog.controller;

import RandevuApp.api.CustomResponseBody;
import RandevuApp.domain.service_catalog.dto.CreateServiceCatalogRequest;
import RandevuApp.domain.service_catalog.dto.ServiceCatalogResponse;
import RandevuApp.domain.service_catalog.dto.UpdateServiceCatalogRequest;
import RandevuApp.domain.service_catalog.service.IServiceCatalogService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/service-catalogs")
@RequiredArgsConstructor
public class ServiceCatalogController {

    private final IServiceCatalogService serviceCatalogService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CustomResponseBody<ServiceCatalogResponse>> createCatalog(@Valid @RequestBody CreateServiceCatalogRequest request) {
        ServiceCatalogResponse response = serviceCatalogService.createCatalog(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(CustomResponseBody.ok(response, "Service catalog created successfully"));
    }

    @PutMapping("/{catalogId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CustomResponseBody<ServiceCatalogResponse>> updateCatalog(
            @PathVariable Long catalogId,
            @Valid @RequestBody UpdateServiceCatalogRequest request) {
        ServiceCatalogResponse response = serviceCatalogService.updateCatalog(catalogId, request);
        return ResponseEntity.ok(CustomResponseBody.ok(response, "Service catalog updated successfully"));
    }

    @DeleteMapping("/{catalogId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CustomResponseBody<Void>> deleteCatalog(@PathVariable Long catalogId) {
        serviceCatalogService.deleteCatalog(catalogId);
        return ResponseEntity.ok(CustomResponseBody.ok(null, "Service catalog deleted successfully"));
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CustomResponseBody<List<ServiceCatalogResponse>>> getAllCatalogs() {
        List<ServiceCatalogResponse> responses = serviceCatalogService.getAllCatalogs();
        return ResponseEntity.ok(CustomResponseBody.ok(responses, "Service catalogs retrieved successfully"));
    }
}
