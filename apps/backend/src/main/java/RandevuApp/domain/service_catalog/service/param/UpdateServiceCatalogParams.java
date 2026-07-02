package RandevuApp.domain.service_catalog.service.param;

import RandevuApp.exceptions.client.InvalidInputException;

import java.math.BigDecimal;

public record UpdateServiceCatalogParams(
        String name,
        String description,
        Integer durationInMinutes,
        BigDecimal price,
        Boolean active
) {
    public UpdateServiceCatalogParams {
        if (durationInMinutes != null && durationInMinutes < 5) {
            throw new InvalidInputException("Süre en az 5 dakika olmalıdır.");
        }
        if (price != null && price.compareTo(BigDecimal.ZERO) < 0) {
            throw new InvalidInputException("Fiyat 0'dan küçük olamaz.");
        }
    }
}
