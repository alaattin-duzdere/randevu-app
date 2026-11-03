package RandevuApp.domain.appointment.service.impl;

import RandevuApp.domain.appointment.dto.AppointmentResponse;
import RandevuApp.domain.appointment.dto.BusinessAppointmentFilter;
import RandevuApp.domain.appointment.dto.RescheduleAppointmentRequest;
import RandevuApp.domain.appointment.mapper.AppointmentMapper;
import RandevuApp.domain.appointment.model.Appointment;
import RandevuApp.domain.appointment.model.AppointmentStatus;
import RandevuApp.domain.appointment.service.IAppointmentDomainService;
import RandevuApp.domain.appointment.service.IBusinessOwnerAppointmentService;
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
public class BusinessOwnerAppointmentServiceImpl implements IBusinessOwnerAppointmentService {

    private final IAppointmentDomainService appointmentDomainService;
    private final AppointmentMapper appointmentMapper;

    @Override
    public AppointmentResponse get(Long businessId, Long appointmentId) {
        return appointmentMapper.mapToResponse(loadForBusiness(businessId, appointmentId));
    }

    @Override
    public Page<AppointmentResponse> list(Long businessId, BusinessAppointmentFilter filter, Pageable pageable) {

        AppointmentFindCriteria criteria = AppointmentFindCriteria.builder()
                .businessId(businessId)
                .staffId(filter != null ? filter.staffId() : null)
                .serviceId(filter != null ? filter.serviceId() : null)
                .statuses(filter != null ? filter.statuses() : null)
                .startDate(filter != null ? filter.startDate() : null)
                .endDate(filter != null ? filter.endDate() : null)
                .build();

        return appointmentDomainService.findAppointmentByCriteria(criteria, pageable).map(appointmentMapper::mapToResponse);
    }

    @Override
    public AppointmentResponse reschedule(Long businessId, Long appointmentId, RescheduleAppointmentRequest request) {
        return null;
    }

    @Override
    public AppointmentResponse updateStatus(Long businessId, Long appointmentId, AppointmentStatus newStatus) {
        Appointment appointment = loadForBusiness(businessId, appointmentId);
        appointmentDomainService.transitionStatus(appointment, newStatus);
        return appointmentMapper.mapToResponse(appointment);
    }

    private Appointment loadForBusiness(Long businessId, Long appointmentId) {
        Appointment appointment = appointmentDomainService.getById(appointmentId);

        if (!businessId.equals(appointment.getBusiness().getId())) {
            throw new OwnerMismatchException("Business with id " + businessId + " is not the owner of appointment with id " + appointmentId);
        }

        return appointment;
    }
}
