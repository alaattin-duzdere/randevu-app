package RandevuApp.domain.staff.dto;

import RandevuApp.commons.annotation.ValidColorCode;
import RandevuApp.commons.annotation.ValidPhone;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.List;

@Data
public class CreateStaffRequest {

    @NotBlank(message = "Personel adı boş olamaz")
    private String name;

    private String title;

    @Email(message = "Geçerli bir e-posta adresi giriniz")
    private String email;

    @ValidPhone
    private String phone;

    @ValidColorCode
    private String colorCode;

    private String photo;

    private List<Long> serviceIds;
}