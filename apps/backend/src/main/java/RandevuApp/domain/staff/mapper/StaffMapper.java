package RandevuApp.domain.staff.mapper;

import RandevuApp.domain.staff.dto.StaffResponse;
import RandevuApp.domain.staff.dto.UpdateStaffRequest;
import RandevuApp.domain.staff.model.Staff;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class StaffMapper {

    public StaffResponse entityToResponse(Staff staff) {

        List<Long> serviceIds = new ArrayList<>();
        if (staff.getStaffServices() != null) {
            serviceIds = staff.getStaffServices().stream().map(
                    staffService -> staffService.getServiceOffering().getId()
            ).toList();
        }

        return StaffResponse.builder()
                .id(staff.getId())
                .businessId(staff.getBusiness().getId())
                .name(staff.getName())
                .title(staff.getTitle())
                .email(staff.getEmail())
                .phone(staff.getPhone())
                .colorCode(staff.getColorCode())
                .photo(staff.getPhoto())
                .active(staff.isActive())
                .serviceIds(serviceIds)
                .build();
    }

    public void updateStaffFromDto(UpdateStaffRequest request, Staff staff) {
        if (request.name() != null) {
            staff.setName(request.name());
        }
        if (request.title() != null) {
            staff.setTitle(request.title());
        }
        if (request.email() != null) {
            staff.setEmail(request.email());
        }
        if (request.phone() != null) {
            staff.setPhone(request.phone());
        }
        if (request.colorCode() != null) {
            staff.setColorCode(request.colorCode());
        }
        if (request.photo() != null) {
            staff.setPhoto(request.photo());
        }
        if (request.active() != null) {
            staff.setActive(request.active());
        }
    }
}
