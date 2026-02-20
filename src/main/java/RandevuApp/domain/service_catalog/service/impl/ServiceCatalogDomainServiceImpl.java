package RandevuApp.domain.service_catalog.service.impl;

import RandevuApp.domain.service_catalog.dto.UpdateServiceCatalogRequest;
import RandevuApp.domain.service_catalog.model.ServiceCatalog;
import RandevuApp.domain.service_catalog.repository.ServiceCatalogRepository;
import RandevuApp.domain.service_catalog.service.IServiceCatalogDomainService;
import RandevuApp.exceptions.client.ConflictException;
import RandevuApp.exceptions.client.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ServiceCatalogDomainServiceImpl implements IServiceCatalogDomainService {

    private final ServiceCatalogRepository repository;

    // --- NESNE ÜRETİMİ (FACTORY METHODS) ---

    @Override
    public ServiceCatalog createEntity(String name, String description, Integer duration, BigDecimal price) {
        return ServiceCatalog.builder()
                .name(name)
                .description(description)
                .durationInMinutes(duration)
                .price(price)
                .active(true)
                .build();
    }


    // --- İŞ KURALLARI VE VALİDASYON ---

    @Override
    public void validateCatalogNameIsUnique(String name) {
        if (repository.existsByNameIgnoreCase(name)) {
            throw new ConflictException("Sistemde '" + name + "' adında bir hazır hizmet şablonu zaten mevcut.");
        }
    }

    @Override
    public ServiceCatalog performUpdate(ServiceCatalog catalog, UpdateServiceCatalogRequest request) {
        if (request.getName() != null && !request.getName().isBlank()) {
            catalog.setName(request.getName());
        }
        if (request.getDescription() != null) {
            catalog.setDescription(request.getDescription());
        }
        if (request.getDurationInMinutes() != null) {
            catalog.setDurationInMinutes(request.getDurationInMinutes());
        }
        if (request.getPrice() != null) {
            catalog.setPrice(request.getPrice());
        }
        if (request.getActive() != null) {
            catalog.setActive(request.getActive());
        }

        return catalog;
    }


    // --- VERİTABANI ERİŞİMİ (REPOSITORY WRAPPERS) ---

    @Override
    public ServiceCatalog save(ServiceCatalog catalog) {
        return repository.save(catalog);
    }

    @Override
    public ServiceCatalog getById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Katalog şablonu bulunamadı", "id", id));
    }

    @Override
    public List<ServiceCatalog> getAllActiveCatalogs() {
        return repository.findAllByActiveTrue();
    }

    @Override
    public void delete(ServiceCatalog catalog) {
        repository.delete(catalog);
    }
}