package RandevuApp.domain.staff.controller;

import RandevuApp.api.CustomResponseBody;
import RandevuApp.domain.staff.dto.CreateStaffRequest;
import RandevuApp.domain.staff.dto.StaffResponse;
import RandevuApp.domain.staff.dto.UpdateStaffRequest;
import RandevuApp.domain.staff.service.IStaffService;
import RandevuApp.utils.SecurityUtils;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/businesses/{businessId}/staffs")
@RequiredArgsConstructor
public class StaffController {

    private final IStaffService staffService;

    @PostMapping
    public ResponseEntity<CustomResponseBody<StaffResponse>> createStaff(
            @PathVariable Long businessId,
            @Valid @RequestBody CreateStaffRequest request) {
        
        Long ownerId = SecurityUtils.getCurrentUserId();
        StaffResponse response = staffService.createStaff(businessId, request, ownerId);
        return ResponseEntity.status(HttpStatus.CREATED).body(CustomResponseBody.ok(response, "Staff created successfully"));
    }

    @PutMapping("/{staffId}")
    public ResponseEntity<CustomResponseBody<StaffResponse>> updateStaff(
            @PathVariable Long businessId,
            @PathVariable Long staffId,
            @Valid @RequestBody UpdateStaffRequest request) {

        Long ownerId = SecurityUtils.getCurrentUserId();
        StaffResponse response = staffService.updateStaff(businessId, staffId, request, ownerId);
        return ResponseEntity.ok(CustomResponseBody.ok(response, "Staff updated successfully"));
    }

    @DeleteMapping("/{staffId}")
    public ResponseEntity<CustomResponseBody<Void>> deleteStaff(
            @PathVariable Long businessId,
            @PathVariable Long staffId) {

        Long ownerId = SecurityUtils.getCurrentUserId();
        staffService.deleteStaff(businessId, staffId, ownerId);
        return ResponseEntity.ok(CustomResponseBody.ok(null, "Staff deleted successfully"));
    }

    @GetMapping
    public ResponseEntity<CustomResponseBody<List<StaffResponse>>> getAllStaffOfBusiness(
            @PathVariable Long businessId) {
        List<StaffResponse> responses = staffService.getAllStaffOfBusiness(businessId);
        return ResponseEntity.ok(CustomResponseBody.ok(responses, "Staff list retrieved successfully"));
    }

    @GetMapping("/{staffId}")
    public ResponseEntity<CustomResponseBody<StaffResponse>> getStaffById(
            @PathVariable Long businessId,
            @PathVariable Long staffId) {

        StaffResponse response = staffService.getStaffById(businessId, staffId);
        return ResponseEntity.ok(CustomResponseBody.ok(response, "Staff details retrieved successfully"));
    }

    @PutMapping("/{staffId}/services")
    public ResponseEntity<CustomResponseBody<Void>> assignServicesToStaff(
            @PathVariable Long businessId,
            @PathVariable Long staffId,
            @RequestBody List<Long> serviceIds) {

        Long ownerId = SecurityUtils.getCurrentUserId();
        staffService.assignServicesToStaff(businessId, staffId, serviceIds, ownerId);
        return ResponseEntity.ok(CustomResponseBody.ok(null, "Services assigned to staff successfully"));
    }
}
