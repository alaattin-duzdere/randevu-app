package RandevuApp.domain.business.service.params;

import RandevuApp.domain.business.model.Address;

public record CreateBusinessParams(
        String name,
        Address address,
        String description,
        String timeZone,
        String slug
) {
    public CreateBusinessParams {
        if (name == null || name.trim().length() < 2) {
            throw new IllegalArgumentException("Business name must be at least 2 characters long");
        }
        if (address == null) {
            throw new IllegalArgumentException("Address details are required");
        }
        if (timeZone == null || timeZone.trim().length() < 2) {
            throw new IllegalArgumentException("Time zone must be at least 2 characters long");
        }
        if (slug == null || slug.trim().length() < 2) {
            throw new IllegalArgumentException("Slug must be at least 2 characters long");
        }
    }
}
