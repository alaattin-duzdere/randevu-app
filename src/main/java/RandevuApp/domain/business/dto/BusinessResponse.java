package RandevuApp.domain.business.dto;

import RandevuApp.domain.business.model.BusinessSettings;
import RandevuApp.domain.service_offering.model.ServiceOffering;
import RandevuApp.domain.staff.model.Staff;
import RandevuApp.domain.user.dto.UserResponse;
import RandevuApp.domain.user.model.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class BusinessResponse {

    private Long id;

    private String name;

    private String address;

    private String slug;

    private String timeZone;

    private String description;

    private boolean active = true;

    private UserResponse owner;

    private BusinessSettingsResponse businessSettings;

    private List<Staff> staffList;

    private List<ServiceOffering> serviceList;
}
