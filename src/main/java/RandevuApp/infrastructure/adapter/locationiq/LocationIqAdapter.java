package RandevuApp.infrastructure.adapter.locationiq;

import RandevuApp.commons.model.LocationProvider;
import RandevuApp.integration.location.port.IGeocodingPort;
import RandevuApp.domain.business.dto.GeoLocationResult;
import RandevuApp.infrastructure.adapter.locationiq.dto.LocationIqResponse;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;

import java.util.Optional;

@Slf4j
@Service
public class LocationIqAdapter implements IGeocodingPort {

    private final RestClient restClient;
    private final String apiKey;
    private final String baseUrl;

    public LocationIqAdapter(
            RestClient.Builder restClientBuilder,
            @Value("${locationiq.api.key}") String apiKey) {
        this.restClient = restClientBuilder.build();
        this.apiKey = apiKey;
        this.baseUrl = "https://us1.locationiq.com/v1/lookup?key={key}&osm_ids={osmIds}&format=json&addressdetails=1";
    }

    @Override
    public Optional<GeoLocationResult> getPlaceDetailsById(String externalLocationId) {
        log.warn("Sıkı Tutun!! LocationIQ API'ye istek atılıyor.");
        try {
            LocationIqResponse[] responseArray = restClient.get()
                    .uri(baseUrl, apiKey, externalLocationId)
                    .retrieve()
                    .body(LocationIqResponse[].class);

            if (responseArray != null && responseArray.length > 0) {
                LocationIqResponse apiResponse = responseArray[0];
                return Optional.of(mapToDomainResult(externalLocationId, apiResponse));
            }

            // TODO: what if array has atleast 2 elements
            return Optional.empty();

        } catch (HttpClientErrorException.NotFound | HttpClientErrorException.BadRequest e) {
            log.warn("LocationIQ API'den beklenen lokasyon bulunamadı. ID: {}", externalLocationId);
            return Optional.empty();
        } catch (Exception e) {
            log.error("LocationIQ API'ye istek atılırken beklenmeyen bir hata oluştu. ID: {}", externalLocationId, e);
            return Optional.empty();
        }
    }

    private GeoLocationResult mapToDomainResult(String externalLocationId, LocationIqResponse response) {
        String city = null;
        String district = null;

        if (response.getAddress() != null) {
            city = extractCity(response.getAddress());
            district = extractDistrict(response.getAddress());
        }

        return GeoLocationResult.builder()
                .externalLocationId(externalLocationId)
                .provider(LocationProvider.LOCATION_IQ)
                .latitude(Double.parseDouble(response.getLat()))
                .longitude(Double.parseDouble(response.getLon()))
                .formattedAddress(response.getDisplayName())
                .city(city)
                .district(district)
                .build();
    }

    private String extractCity(LocationIqResponse.LocationIqAddress address) {
        if (address.getCity() != null) return address.getCity();
        if (address.getProvince() != null) return address.getProvince();
        return "Bilinmiyor";
    }

    private String extractDistrict(LocationIqResponse.LocationIqAddress address) {
        if (address.getCounty() != null) return address.getCounty();
        if (address.getDistrict() != null) return address.getDistrict();
        if (address.getSuburb() != null) return address.getSuburb();
        if (address.getTown() != null) return address.getTown();
        return "Bilinmiyor";
    }
}