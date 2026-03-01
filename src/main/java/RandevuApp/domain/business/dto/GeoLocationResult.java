package RandevuApp.domain.business.dto;

import RandevuApp.commons.model.LocationProvider;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class GeoLocationResult {
    private String externalLocationId;
    private LocationProvider provider;

    private String city;
    private String district;
    private Double latitude;
    private Double longitude;
    private String formattedAddress;
}