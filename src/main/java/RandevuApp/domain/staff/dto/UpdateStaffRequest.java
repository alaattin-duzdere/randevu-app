package RandevuApp.domain.staff.dto;

import RandevuApp.commons.annotation.ValidColorCode;
import RandevuApp.commons.annotation.ValidPhone;
import jakarta.validation.constraints.Email;

import java.util.List;

public record UpdateStaffRequest(
        String name,
        String title,
        @Email String email,
        @ValidPhone String phone,
        @ValidColorCode String colorCode,
        String photo,
        Boolean active,
        List<Long> serviceIds
) {}
