package RandevuApp.domain.staff.controller;

import RandevuApp.domain.staff.dto.CreateStaffRequest;
import RandevuApp.domain.staff.dto.StaffResponse;
import RandevuApp.domain.staff.dto.UpdateStaffRequest;
import RandevuApp.domain.staff.service.IStaffService;
import RandevuApp.utils.SecurityUtils;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/businesses/{businessId}/staffs")
@RequiredArgsConstructor
public class StaffController {

    private final IStaffService staffService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public StaffResponse createStaff(
            @PathVariable Long businessId,
            @Valid @RequestBody CreateStaffRequest request) {
        
        Long ownerId = SecurityUtils.getCurrentUserId();
        return staffService.createStaff(businessId, request, ownerId);
    }

    @PutMapping("/{staffId}")
    public StaffResponse updateStaff(
            @PathVariable Long businessId,
            @PathVariable Long staffId,
            @Valid @RequestBody UpdateStaffRequest request) {

        Long ownerId = SecurityUtils.getCurrentUserId();
        return staffService.updateStaff(businessId, staffId, request, ownerId);
    }

    @DeleteMapping("/{staffId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteStaff(
            @PathVariable Long businessId,
            @PathVariable Long staffId) {

        Long ownerId = SecurityUtils.getCurrentUserId();
        staffService.deleteStaff(businessId, staffId, ownerId);
    }

    @GetMapping("/{staffId}")
    public StaffResponse getStaffById(
            @PathVariable Long businessId,
            @PathVariable Long staffId) {

        return staffService.getStaffById(businessId, staffId);
    }

    @GetMapping
    public List<StaffResponse> getAllStaffOfBusiness(
            @PathVariable Long businessId) {
        return staffService.getAllStaffOfBusiness(businessId);
    }

    @PutMapping("/{staffId}/services")
    public void assignServicesToStaff(
            @PathVariable Long businessId,
            @PathVariable Long staffId,
            @RequestBody List<Long> serviceIds) {

        Long ownerId = SecurityUtils.getCurrentUserId();
        staffService.assignServicesToStaff(businessId, staffId, serviceIds, ownerId);
    }
}
