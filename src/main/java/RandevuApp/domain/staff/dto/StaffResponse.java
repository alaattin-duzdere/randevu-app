package RandevuApp.domain.staff.dto;

import lombok.Builder;

import java.util.List;

@Builder
public record StaffResponse(
        Long id,
        Long businessId,
        String name,
        String title,
        String email,
        String phone,
        String colorCode,
        String photo,
        boolean active,
        List<Long> serviceIds
) {}
