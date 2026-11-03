package RandevuApp.domain.appointment.service.impl;

import RandevuApp.domain.appointment.dto.AdminCreateAppointmentRequest;
import RandevuApp.domain.appointment.dto.AppointmentResponse;
import RandevuApp.domain.appointment.dto.RescheduleAppointmentRequest;
import RandevuApp.domain.appointment.mapper.AppointmentMapper;
import RandevuApp.domain.appointment.model.Appointment;
import RandevuApp.domain.appointment.model.AppointmentStatus;
import RandevuApp.domain.appointment.service.AppointmentBookingService;
import RandevuApp.domain.appointment.service.IAdminAppointmentService;
import RandevuApp.domain.appointment.service.IAppointmentDomainService;
import RandevuApp.domain.appointment.service.params.AppointmentFindCriteria;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class AdminAppointmentServiceImpl implements IAdminAppointmentService {

    private final IAppointmentDomainService appointmentDomainService;
    private final AppointmentBookingService appointmentBookingService;
    private final AppointmentMapper appointmentMapper;

    @Override
    public AppointmentResponse createAppointment(AdminCreateAppointmentRequest request) {
        Appointment appointment = appointmentBookingService.book(request.toCommand());
        return appointmentMapper.mapToResponse(appointment);
    }

    @Override
    public void updateAppointmentStatus(Long appointmentId, AppointmentStatus newStatus) {
        Appointment appointment = appointmentDomainService.getById(appointmentId);
        appointmentDomainService.transitionStatus(appointment,newStatus);
    }

    public void rescheduleAppointment(Long appointmentId, RescheduleAppointmentRequest request) {
        Appointment appointment= appointmentDomainService.getById(appointmentId);
        appointmentDomainService.reschedule(appointment, request.newStartTime(),request.newEndTime());
    }

    @Override
    public AppointmentResponse getAppointment(Long appointmentId) {
        return appointmentMapper.mapToResponse(appointmentDomainService.getById(appointmentId));
    }

    @Override
    public Page<AppointmentResponse> getAppointments(AppointmentFindCriteria criteria, Pageable pageable) {
        return appointmentDomainService.findAppointmentByCriteria(criteria, pageable).map(appointmentMapper::mapToResponse);
    }
}
