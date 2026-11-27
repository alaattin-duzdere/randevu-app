package RandevuApp.domain.business.dto;

import RandevuApp.domain.business.model.BusinessType;

public record BusinessSearchRequest(
        String query,
        String address,
        Boolean active,
        BusinessType businessType
) {
    public BusinessSearchRequest {
        if (active == null) active = true;
    }
}