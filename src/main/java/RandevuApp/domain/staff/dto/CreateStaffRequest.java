package RandevuApp.domain.staff.dto;

import RandevuApp.commons.annotation.ValidColorCode;
import RandevuApp.commons.annotation.ValidPhone;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

import java.util.List;

public record CreateStaffRequest(
        @NotBlank(message = "Personel adı boş olamaz") String name,
        String title,
        @Email(message = "Geçerli bir e-posta adresi giriniz") String email,
        @ValidPhone String phone,
        @ValidColorCode String colorCode,
        String photo,
        List<Long> serviceIds
) {}
