package RandevuApp.domain.business.dto;

import RandevuApp.domain.business.model.Address;
import RandevuApp.domain.user.dto.UserResponse;

import java.util.List;

public record BusinessResponse(
        Long id,
        String name,
        Address address,
        String slug,
        String timeZone,
        String description,
        boolean active,
        UserResponse owner,
        BusinessSettingsResponse businessSettings,
        List<Long> staffIds,
        List<Long> serviceIds
) {}
