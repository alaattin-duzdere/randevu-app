package RandevuApp.domain.business.dto;

import RandevuApp.commons.model.LocationProvider;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AddressDto {

    @NotBlank(message = "Harici lokasyon ID'si (externalLocationId) boş bırakılamaz")
    private String externalLocationId;

    @NotNull(message = "Lokasyon sağlayıcısı (provider) belirtilmelidir")
    private LocationProvider provider; // MAPBOX, GOOGLE_PLACES etc.
}