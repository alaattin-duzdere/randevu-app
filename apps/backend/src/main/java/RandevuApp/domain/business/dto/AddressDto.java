package RandevuApp.domain.business.dto;

import RandevuApp.commons.model.LocationProvider;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record AddressDto(
        @NotBlank(message = "Harici lokasyon ID'si (externalLocationId) boş bırakılamaz") String externalLocationId,
        @NotNull(message = "Lokasyon sağlayıcısı (provider) belirtilmelidir") LocationProvider provider
) {}
