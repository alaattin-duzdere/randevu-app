package RandevuApp.domain.service_catalog.service.impl;

import RandevuApp.domain.service_catalog.dto.CreateServiceCatalogRequest;
import RandevuApp.domain.service_catalog.dto.ServiceCatalogResponse;
import RandevuApp.domain.service_catalog.dto.UpdateServiceCatalogRequest;
import RandevuApp.domain.service_catalog.model.ServiceCatalog;
import RandevuApp.domain.service_catalog.service.IServiceCatalogDomainService;
import RandevuApp.domain.service_catalog.service.IServiceCatalogService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ServiceCatalogServiceImpl implements IServiceCatalogService {

    private final IServiceCatalogDomainService catalogDomainService;

    @Override
    @Transactional
    public ServiceCatalogResponse createCatalog(CreateServiceCatalogRequest request) {
        log.info("Yeni hizmet kataloğu oluşturuluyor: {}", request.getName());

        // 1. İş Kuralı: İsim benzersiz mi?
        catalogDomainService.validateCatalogNameIsUnique(request.getName());

        // 2. Entity'yi oluştur
        ServiceCatalog catalog = catalogDomainService.createEntity(
                request.getName(),
                request.getDescription(),
                request.getDurationInMinutes(),
                request.getPrice()
        );

        // 3. Veritabanına kaydet
        ServiceCatalog savedCatalog = catalogDomainService.save(catalog);

        // 4. DTO'ya çevir ve dön
        return mapToResponse(savedCatalog);
    }

    @Override
    @Transactional
    public ServiceCatalogResponse updateCatalog(Long catalogId, UpdateServiceCatalogRequest request) {
        log.info("Hizmet kataloğu güncelleniyor. ID: {}", catalogId);

        // 1. Kataloğu bul (Yoksa Domain Service Exception fırlatır)
        ServiceCatalog catalog = catalogDomainService.getById(catalogId);

        // 2. İsim değişiyorsa benzersizlik kontrolü yap
        if (request.getName() != null && !request.getName().equals(catalog.getName())) {
            catalogDomainService.validateCatalogNameIsUnique(request.getName());
        }

        // 3. Güncelleme işlemini uygula
        ServiceCatalog updatedCatalog = catalogDomainService.performUpdate(catalog, request);

        // 4. Kaydet
        catalogDomainService.save(updatedCatalog);

        return mapToResponse(updatedCatalog);
    }

    @Override
    @Transactional
    public void deleteCatalog(Long catalogId) {
        log.info("Hizmet kataloğu siliniyor. ID: {}", catalogId);

        ServiceCatalog catalog = catalogDomainService.getById(catalogId);

        // TODO: Eğer bu katalogdan kopyalanan ServiceOffering'lerin referansını
        // tutuyorsan (ilişkisel bir durum varsa) burada kontrol etmen gerekebilir.
        // Ancak şu anki yapımızda (Klonlama/Clone mantığı) aralarında kopmaz bir bağ yok,
        // o yüzden güvenle silebiliriz.

        catalogDomainService.delete(catalog);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ServiceCatalogResponse> getAllCatalogs() {
        List<ServiceCatalog> catalogs = catalogDomainService.getAllActiveCatalogs();

        return catalogs.stream()
                .map(this::mapToResponse)
                .toList();
    }

    // --- HELPER METHOD (DTO Dönüşümü) ---
    private ServiceCatalogResponse mapToResponse(ServiceCatalog catalog) {
        return ServiceCatalogResponse.builder()
                .id(catalog.getId())
                .name(catalog.getName())
                .description(catalog.getDescription())
                .durationInMinutes(catalog.getDurationInMinutes())
                .price(catalog.getPrice())
                .active(catalog.isActive())
                .build();
    }
}