package RandevuApp.domain.business.dto;

import lombok.Data;

@Data
public class BusinessSearchRequest {
    private String query;

    private String address;

    private Boolean active = true;
}
