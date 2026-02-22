package RandevuApp.domain.staff.dto;

import RandevuApp.commons.annotation.ValidColorCode;
import RandevuApp.commons.annotation.ValidPhone;
import jakarta.validation.constraints.Email;
import lombok.Data;
import java.util.List;

@Data
public class UpdateStaffRequest {

    private String name;

    private String title;

    @Email
    private String email;

    @ValidPhone
    private String phone;

    @ValidColorCode
    private String colorCode;

    private String photo;

    private Boolean active;

    private List<Long> serviceIds;
}