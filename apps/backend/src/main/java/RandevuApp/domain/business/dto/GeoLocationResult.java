package RandevuApp.domain.business.dto;

import RandevuApp.commons.model.LocationProvider;
import lombok.Builder;

@Builder
public record GeoLocationResult(
        String externalLocationId,
        LocationProvider provider,
        String city,
        String district,
        Double latitude,
        Double longitude,
        String formattedAddress
) {}
