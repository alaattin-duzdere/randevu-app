package RandevuApp.domain.appointment.controller;

import RandevuApp.domain.appointment.dto.AppointmentResponse;
import RandevuApp.domain.appointment.dto.BusinessAppointmentFilter;
import RandevuApp.domain.appointment.dto.RescheduleAppointmentRequest;
import RandevuApp.domain.appointment.dto.UpdateAppointmentStatusRequest;
import RandevuApp.domain.appointment.model.AppointmentStatus;
import RandevuApp.domain.appointment.service.IBusinessOwnerAppointmentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/businesses/{businessId}/appointments")
@RequiredArgsConstructor
@PreAuthorize("hasRole('BUSINESS_OWNER') and @businessAccess.isOwner(#businessId)")
public class BusinessOwnerAppointmentController {

    private final IBusinessOwnerAppointmentService businessOwnerAppointmentService;

    @GetMapping("/{appointmentId}")
    public AppointmentResponse getAppointment(
            @PathVariable Long businessId,
            @PathVariable Long appointmentId) {
        return businessOwnerAppointmentService.get(businessId, appointmentId);
    }

    @GetMapping
    public Page<AppointmentResponse> getAppointments(
            @PathVariable Long businessId,
            @ModelAttribute BusinessAppointmentFilter filter,
            @PageableDefault(size = 20, sort = "startTime") Pageable pageable) {

        return businessOwnerAppointmentService.list(businessId, filter, pageable);
    }

    @PutMapping("/{appointmentId}/schedule")
    public AppointmentResponse rescheduleAppointment(
            @PathVariable Long businessId,
            @PathVariable Long appointmentId,
            @Valid @RequestBody RescheduleAppointmentRequest request) {
        return businessOwnerAppointmentService.reschedule(businessId, appointmentId, request);
    }

    @PatchMapping("/{appointmentId}/status")
    public AppointmentResponse updateAppointmentStatus(
            @PathVariable Long businessId,
            @PathVariable Long appointmentId,
            @Valid @RequestBody UpdateAppointmentStatusRequest request) {
        return businessOwnerAppointmentService.updateStatus(businessId, appointmentId, request.status());
    }

    @PatchMapping("/{appointmentId}/confirm")
    public AppointmentResponse confirmAppointment(
            @PathVariable Long businessId,
            @PathVariable Long appointmentId) {
        return businessOwnerAppointmentService.updateStatus(businessId, appointmentId, AppointmentStatus.CONFIRMED);
    }

    @PatchMapping("/{appointmentId}/reject")
    public AppointmentResponse rejectAppointment(
            @PathVariable Long businessId,
            @PathVariable Long appointmentId) {
        return businessOwnerAppointmentService.updateStatus(businessId, appointmentId, AppointmentStatus.REJECTED);
    }

    @PatchMapping("/{appointmentId}/cancel")
    public AppointmentResponse cancelAppointment(
            @PathVariable Long businessId,
            @PathVariable Long appointmentId) {
        return businessOwnerAppointmentService.updateStatus(businessId, appointmentId, AppointmentStatus.CANCELLED);
    }

    @PatchMapping("/{appointmentId}/complete")
    public AppointmentResponse completeAppointment(
            @PathVariable Long businessId,
            @PathVariable Long appointmentId) {
        return businessOwnerAppointmentService.updateStatus(businessId, appointmentId, AppointmentStatus.COMPLETED);
    }
}
