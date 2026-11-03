package RandevuApp.domain.appointment.controller;

import RandevuApp.domain.appointment.dto.AdminCreateAppointmentRequest;
import RandevuApp.domain.appointment.dto.AppointmentResponse;
import RandevuApp.domain.appointment.dto.RescheduleAppointmentRequest;
import RandevuApp.domain.appointment.dto.UpdateAppointmentStatusRequest;
import RandevuApp.domain.appointment.service.IAdminAppointmentService;
import RandevuApp.domain.appointment.service.params.AppointmentFindCriteria;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/appointments")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class AdminAppointmentController {

    private final IAdminAppointmentService adminAppointmentService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public AppointmentResponse createAppointment(@Valid @RequestBody AdminCreateAppointmentRequest request) {
        return adminAppointmentService.createAppointment(request);
    }

    @GetMapping("/{appointmentId}")
    public AppointmentResponse getAppointmentById(@PathVariable Long appointmentId) {
        return adminAppointmentService.getAppointment(appointmentId);
    }

    @GetMapping
    public Page<AppointmentResponse> getAppointments(
            @ModelAttribute AppointmentFindCriteria criteria,
            @PageableDefault(size = 20, sort = "startTime") Pageable pageable) {
        return adminAppointmentService.getAppointments(criteria, pageable);
    }

    @PutMapping("/{appointmentId}/schedule")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void rescheduleAppointment(
            @PathVariable Long appointmentId,
            @Valid @RequestBody RescheduleAppointmentRequest request) {
        adminAppointmentService.rescheduleAppointment(appointmentId, request);
    }

    @PatchMapping("/{appointmentId}/status")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateAppointmentStatus(
            @PathVariable Long appointmentId,
            @Valid @RequestBody UpdateAppointmentStatusRequest request) {
        adminAppointmentService.updateAppointmentStatus(appointmentId, request.status());
    }
}
