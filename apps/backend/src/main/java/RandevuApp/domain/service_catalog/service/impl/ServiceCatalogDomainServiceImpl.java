package RandevuApp.domain.service_catalog.service.impl;

import RandevuApp.domain.service_catalog.model.ServiceCatalog;
import RandevuApp.domain.service_catalog.repository.ServiceCatalogRepository;
import RandevuApp.domain.service_catalog.service.IServiceCatalogDomainService;
import RandevuApp.domain.service_catalog.service.param.CreateServiceCatalogParams;
import RandevuApp.domain.service_catalog.service.param.UpdateServiceCatalogParams;
import RandevuApp.exceptions.client.ConflictException;
import RandevuApp.exceptions.client.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ServiceCatalogDomainServiceImpl implements IServiceCatalogDomainService {

    private final ServiceCatalogRepository repository;

    // --- NESNE ÜRETİMİ (FACTORY METHODS) ---

    @Override
    public ServiceCatalog createEntity(CreateServiceCatalogParams params) {
        return ServiceCatalog.builder()
                .name(params.name())
                .description(params.description())
                .durationInMinutes(params.duration())
                .price(params.price())
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
    public ServiceCatalog performUpdate(ServiceCatalog catalog, UpdateServiceCatalogParams params) {
        if (params.name() != null && !params.name().isBlank()) {
            catalog.setName(params.name());
        }
        if (params.description() != null) {
            catalog.setDescription(params.description());
        }
        if (params.durationInMinutes() != null) {
            catalog.setDurationInMinutes(params.durationInMinutes());
        }
        if (params.price() != null) {
            catalog.setPrice(params.price());
        }
        if (params.active() != null) {
            catalog.setActive(params.active());
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