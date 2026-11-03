package RandevuApp.domain.appointment.service.impl;

import RandevuApp.domain.appointment.dto.AppointmentResponse;
import RandevuApp.domain.appointment.dto.UserAppointmentFilter;
import RandevuApp.domain.appointment.dto.UserCreateAppointmentRequest;
import RandevuApp.domain.appointment.mapper.AppointmentMapper;
import RandevuApp.domain.appointment.model.Appointment;
import RandevuApp.domain.appointment.service.AppointmentBookingService;
import RandevuApp.domain.appointment.service.IAppointmentDomainService;
import RandevuApp.domain.appointment.service.IUserAppointmentService;
import RandevuApp.domain.appointment.service.params.AppointmentFindCriteria;
import RandevuApp.exceptions.client.OwnerMismatchException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class UserAppointmentServiceImpl implements IUserAppointmentService {

    private final IAppointmentDomainService domainService;
    private final AppointmentMapper appointmentMapper;
    private final AppointmentBookingService appointmentBookingService;

    @Override
    public AppointmentResponse createAppointment(Long currentUserId, UserCreateAppointmentRequest request) {
        Appointment appointment = appointmentBookingService.book(request.toCommand(currentUserId));
        return appointmentMapper.mapToResponse(appointment);
    }

    @Override
    @Transactional(readOnly = true)
    public AppointmentResponse getMyAppointment(Long currentUserId, Long appointmentId) {
        return appointmentMapper.mapToResponse(loadOwned(currentUserId, appointmentId));
    }

    @Override
    @Transactional(readOnly = true)
    public Page<AppointmentResponse> getMyAppointments(Long currentUserId, UserAppointmentFilter filter, Pageable pageable) {

        AppointmentFindCriteria criteria = AppointmentFindCriteria.builder()
                .userId(currentUserId)
                .staffId(filter.staffId())
                .businessId(filter.businessId())
                .serviceId(filter.serviceId())
                .statuses(filter.statuses())
                .startDate(filter.startDate())
                .endDate(filter.endDate())
                .build();
        return domainService.findAppointmentByCriteria(criteria, pageable).map(appointmentMapper::mapToResponse);
    }

    private Appointment loadOwned(Long userId, Long appointmentId) {
        Appointment appointment = domainService.getById(appointmentId);

        if (userId == null || appointment.getUser() == null || !userId.equals(appointment.getUser().getId())) {
            throw new OwnerMismatchException("User with id " + userId + " is not the owner of appointment with id " + appointmentId);
        }
        return appointment;
    }
}