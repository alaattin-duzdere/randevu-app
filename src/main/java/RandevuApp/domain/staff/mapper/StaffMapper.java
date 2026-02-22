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
        if (request.getName() != null) {
            staff.setName(request.getName());
        }
        if (request.getTitle() != null) {
            staff.setTitle(request.getTitle());
        }
        if (request.getEmail() != null) {
            staff.setEmail(request.getEmail());
        }
        if (request.getPhone() != null) {
            staff.setPhone(request.getPhone());
        }
        if (request.getColorCode() != null) {
            staff.setColorCode(request.getColorCode());
        }
        if (request.getPhoto() != null) {
            staff.setPhoto(request.getPhoto());
        }
        if (request.getActive() != null) {
            staff.setActive(request.getActive());
        }
    }
}
