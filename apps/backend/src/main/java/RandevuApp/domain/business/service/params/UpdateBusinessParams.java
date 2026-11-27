package RandevuApp.domain.business.service.params;

import RandevuApp.domain.business.model.BusinessType;

public record UpdateBusinessParams(
        String name,
        String description,
        String timeZone,
        Boolean active,
        BusinessType businessType
) {
    public UpdateBusinessParams {
        if (name == null) {
            throw new IllegalArgumentException("Name is required");
        }
        if (description == null) {
            throw new IllegalArgumentException("Description is required");
        }
        if (timeZone == null) {
            throw new IllegalArgumentException("Time zone is required");
        }
        if (businessType == null) {
            throw new IllegalArgumentException("Business type is required");
        }
    }
}
