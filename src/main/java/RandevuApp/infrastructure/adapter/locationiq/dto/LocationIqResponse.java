package RandevuApp.infrastructure.adapter.locationiq.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class LocationIqResponse {
    private String lat;
    private String lon;

    @JsonProperty("display_name")
    private String displayName;

    private LocationIqAddress address;

    @Getter
    @Setter
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class LocationIqAddress {
        private String city;
        private String town;
        private String province;
        private String county;
        private String district;
        private String suburb;
    }
}
