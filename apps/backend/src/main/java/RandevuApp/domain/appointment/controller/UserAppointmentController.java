package RandevuApp.domain.appointment.controller;

import RandevuApp.domain.appointment.dto.AppointmentResponse;
import RandevuApp.domain.appointment.dto.UserAppointmentFilter;
import RandevuApp.domain.appointment.dto.UserCreateAppointmentRequest;
import RandevuApp.domain.appointment.service.IUserAppointmentService;
import RandevuApp.utils.SecurityUtils;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/appointments")
@RequiredArgsConstructor
public class UserAppointmentController {

    private final IUserAppointmentService userAppointmentService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public AppointmentResponse createAppointment(
            @Valid @RequestBody UserCreateAppointmentRequest request) {
        Long currentUserId = SecurityUtils.getCurrentUserId();

        return userAppointmentService.createAppointment(currentUserId, request);
    }

    @GetMapping("/{appointmentId}")
    public AppointmentResponse getMyAppointment(@PathVariable Long appointmentId) {
        Long currentUserId = SecurityUtils.getCurrentUserId();
        return userAppointmentService.getMyAppointment(currentUserId, appointmentId);
    }

    @GetMapping("/me")
    public Page<AppointmentResponse> getMyAppointments(
            @ModelAttribute UserAppointmentFilter filter,
            @PageableDefault(size = 10, sort = "startTime") Pageable pageable) {
        Long currentUserId = SecurityUtils.getCurrentUserId();
        return userAppointmentService.getMyAppointments(currentUserId, filter, pageable);
    }
}
