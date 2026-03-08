package RandevuApp.domain.service_catalog.service.param;

import java.math.BigDecimal;

public record CreateServiceCatalogParams(
        String name,
        String description,
        Integer duration,
        BigDecimal price
) {
}
