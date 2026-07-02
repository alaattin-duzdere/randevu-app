package RandevuApp.domain.service_catalog.controller;

import RandevuApp.domain.service_catalog.dto.CreateServiceCatalogRequest;
import RandevuApp.domain.service_catalog.dto.ServiceCatalogResponse;
import RandevuApp.domain.service_catalog.dto.UpdateServiceCatalogRequest;
import RandevuApp.domain.service_catalog.service.IServiceCatalogService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
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
    @ResponseStatus(HttpStatus.CREATED)
    public ServiceCatalogResponse createCatalog(@Valid @RequestBody CreateServiceCatalogRequest request) {
        return serviceCatalogService.createCatalog(request);

    }

    @PutMapping("/{catalogId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ServiceCatalogResponse updateCatalog(
            @PathVariable Long catalogId,
            @Valid @RequestBody UpdateServiceCatalogRequest request) {
        return serviceCatalogService.updateCatalog(catalogId, request);
    }

    @DeleteMapping("/{catalogId}")
    @PreAuthorize("hasRole('ADMIN')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCatalog(@PathVariable Long catalogId) {
        serviceCatalogService.deleteCatalog(catalogId);
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public List<ServiceCatalogResponse> getAllCatalogs() {
        return serviceCatalogService.getAllCatalogs();
    }
}
