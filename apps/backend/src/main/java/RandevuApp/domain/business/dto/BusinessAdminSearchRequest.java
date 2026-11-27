package RandevuApp.domain.business.dto;

import RandevuApp.domain.business.model.BusinessType;

public record BusinessAdminSearchRequest(
        String query,
        String address,
        boolean isDeleted,
        Boolean active,
        BusinessType businessType
) {
    public BusinessAdminSearchRequest {
        if (active == null) active = true;
    }
}