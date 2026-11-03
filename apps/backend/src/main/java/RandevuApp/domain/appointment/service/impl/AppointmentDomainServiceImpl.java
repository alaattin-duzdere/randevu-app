package RandevuApp.domain.appointment.service.impl;

import RandevuApp.domain.appointment.model.Appointment;
import RandevuApp.domain.appointment.model.AppointmentStatus;
import RandevuApp.domain.appointment.repository.AppointmentRepository;
import RandevuApp.domain.appointment.repository.AppointmentSpecs;
import RandevuApp.domain.appointment.service.IAppointmentDomainService;
import RandevuApp.domain.appointment.service.params.AppointmentFindCriteria;
import RandevuApp.domain.appointment.service.params.CreateAppointmentParams;
import RandevuApp.domain.service_offering.model.ServiceOffering;
import RandevuApp.exceptions.client.ConflictException;
import RandevuApp.exceptions.client.InvalidInputException;
import RandevuApp.exceptions.client.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AppointmentDomainServiceImpl implements IAppointmentDomainService {

    private final AppointmentRepository appointmentRepository;

    @Override
    public Page<Appointment> findAppointmentByCriteria(AppointmentFindCriteria criteria, Pageable pageable) {
        Specification<Appointment> spec = buildSpecification(criteria);

        return appointmentRepository.findAll(spec, pageable);
    }

    @Override
    public boolean existsByCriteria(AppointmentFindCriteria criteria) {
        Specification<Appointment> spec = buildSpecification(criteria);
        return appointmentRepository.exists(spec);
    }

    private Specification<Appointment> buildSpecification(AppointmentFindCriteria criteria) {
        Specification<Appointment> spec = Specification.where((root, query, cb) -> cb.conjunction());

        if (criteria == null) {
            return spec;
        }

        if (criteria.userId() != null) {
            spec = spec.and(AppointmentSpecs.hasUserId(criteria.userId()));
        }
        if (criteria.staffId() != null) {
            spec = spec.and(AppointmentSpecs.hasStaffId(criteria.staffId()));
        }
        if (criteria.businessId() != null) {
            spec = spec.and(AppointmentSpecs.hasBusinessId(criteria.businessId()));
        }
        if (criteria.serviceId() != null) {
            spec = spec.and(AppointmentSpecs.hasServiceId(criteria.serviceId()));
        }
        if (criteria.statuses() != null && !criteria.statuses().isEmpty()) {
            spec = spec.and(AppointmentSpecs.hasStatuses(criteria.statuses()));
        }
        if (criteria.startDate() != null) {
            spec = spec.and(AppointmentSpecs.isAfterOrEqual(criteria.startDate()));
        }
        if (criteria.endDate() != null) {
            spec = spec.and(AppointmentSpecs.isBeforeOrEqual(criteria.endDate()));
        }

        return spec;
    }

    @Override
    public Appointment createEntity(CreateAppointmentParams params) {
        Appointment appointment = new Appointment();

        appointment.setUser(params.user());
        appointment.setBusiness(params.business());
        appointment.setStaff(params.staff());
        appointment.setService(params.service());

        appointment.setStartTime(params.startTime());
        appointment.setEndTime(params.endTime());

        appointment.setCustomerName(params.customerName());
        appointment.setCustomerLastName(params.customerLastName());
        appointment.setCustomerPhone(params.customerPhone());

        appointment.setTimeZone(params.timeZone());

        appointment.setPrice(params.service().getPrice());

        appointment.setAppointmentStatus(AppointmentStatus.PENDING); // default status for appointment

        return appointment;
    }

    @Override
    public void transitionStatus(Appointment appointment, AppointmentStatus newStatus) {
        //TODO: Bu metoda zaman ayır
        AppointmentStatus currentStatus = appointment.getAppointmentStatus();

        if (currentStatus == newStatus) {
            return;
        }

        switch (currentStatus) {
            case PENDING:
                if (newStatus != AppointmentStatus.CONFIRMED &&
                        newStatus != AppointmentStatus.REJECTED &&
                        newStatus != AppointmentStatus.CANCELLED) {
                    throwIllegalState(currentStatus, newStatus);
                }
                break;

            case CONFIRMED:
                if (newStatus != AppointmentStatus.COMPLETED &&
                        newStatus != AppointmentStatus.CANCELLED) {
                    throwIllegalState(currentStatus, newStatus);
                }
                break;

            case COMPLETED:
            case CANCELLED:
            case REJECTED:
                throw new ConflictException(
                        "Randevu kalıcı '" + currentStatus + "' statüsündeyken, '" + newStatus + "' yapılamaz."
                );
        }

        appointment.setAppointmentStatus(newStatus);
    }

    private void throwIllegalState(AppointmentStatus current, AppointmentStatus target) {
        throw new ConflictException("'" + current + "' statüsündeki bir randevu '" + target + "' statüsüne geçirilemez.");
    }

    @Override
    public void reschedule(Appointment appointment, LocalDateTime newStartTime, LocalDateTime newEndTime) {
        //TODO: Bu metoda zaman ayır
        appointment.setStartTime(newStartTime);
        appointment.setEndTime(newEndTime);

        if (appointment.getAppointmentStatus() == AppointmentStatus.CONFIRMED) {
            appointment.setAppointmentStatus(AppointmentStatus.PENDING);
        }
    }

    @Override
    public Appointment save(Appointment appointment) {
        return appointmentRepository.save(appointment);
    }

    @Override
    public Appointment getById(Long appointmentId) {
        return appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Appointment", "id", appointmentId));
    }
}
