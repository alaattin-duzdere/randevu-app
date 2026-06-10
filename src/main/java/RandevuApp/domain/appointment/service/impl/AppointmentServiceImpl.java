package RandevuApp.domain.appointment.service.impl;

import RandevuApp.domain.appointment.availability.service.IAvailabilityService;
import RandevuApp.domain.appointment.dto.AppointmentResponse;
import RandevuApp.domain.appointment.dto.CreateAppointmentRequest;
import RandevuApp.domain.appointment.dto.RescheduleAppointmentRequest;
import RandevuApp.domain.appointment.model.Appointment;
import RandevuApp.domain.appointment.model.AppointmentStatus;
import RandevuApp.domain.appointment.service.IAppointmentDomainService;
import RandevuApp.domain.appointment.service.IAppointmentService;
import RandevuApp.domain.appointment.service.params.AppointmentSearchCriteria;
import RandevuApp.domain.appointment.service.params.CreateAppointmentParams;
import RandevuApp.domain.business.model.Business;
import RandevuApp.domain.business.service.IBusinessDomainService;
import RandevuApp.domain.service_offering.model.ServiceOffering;
import RandevuApp.domain.service_offering.service.IServiceOfferingDomainService;
import RandevuApp.domain.staff.model.Staff;
import RandevuApp.domain.staff.service.IStaffDomainService;
import RandevuApp.domain.user.model.User;
import RandevuApp.domain.user.service.IUserDomainService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AppointmentServiceImpl implements IAppointmentService {

    // TODO: domain servicelerdeki find/get metod isimlendirmesi karmaşasından kurtul.

    private final IAppointmentDomainService appointmentDomainService;
    private final IAvailabilityService availabilityService;

    private final IUserDomainService userDomainService;
    private final IBusinessDomainService businessDomainService;
    private final IStaffDomainService staffDomainService;
    private final IServiceOfferingDomainService serviceOfferingDomainService;

    @Override
    @Transactional
    public AppointmentResponse createAppointment(CreateAppointmentRequest request) {

        User user = userDomainService.findUserById(request.getUserId());
        Business business = businessDomainService.getById(request.getBusinessId());
        Staff staff = staffDomainService.getByIdAndBusinessId(request.getStaffId(), request.getBusinessId());
        ServiceOffering serviceOffering = serviceOfferingDomainService.getById(request.getServiceId());

        appointmentDomainService.validateAppointmentDuration(serviceOffering, request.getStartTime(), request.getEndTime());


        availabilityService.validateSlotAvailability(
                business.getId(), staff.getId(), request.getStartTime(), request.getEndTime(), null
        );

        CreateAppointmentParams params = new CreateAppointmentParams(
                user, business, staff, serviceOffering,
                request.getStartTime(), request.getEndTime(),
                request.getCustomerName(), request.getCustomerPhone()
        );

        Appointment appointment = appointmentDomainService.createEntity(params);

        Appointment savedAppointment = appointmentDomainService.save(appointment);

        return mapToResponse(savedAppointment);
    }

    @Override
    @Transactional
    public AppointmentResponse rescheduleAppointment(Long appointmentId, RescheduleAppointmentRequest request) {

        Appointment appointment = appointmentDomainService.getById(appointmentId);

        appointmentDomainService.validateAppointmentDuration(appointment.getService(), request.getNewStartTime(), request.getNewEndTime());

        availabilityService.validateSlotAvailability(
                appointment.getBusiness().getId(),
                appointment.getStaff().getId(),
                request.getNewStartTime(),
                request.getNewEndTime(),
                appointment.getId() // Exclude own id
        );

        appointmentDomainService.reschedule(appointment, request.getNewStartTime(), request.getNewEndTime());

        Appointment updatedAppointment = appointmentDomainService.save(appointment);
        return mapToResponse(updatedAppointment);
    }

    @Override
    @Transactional
    public AppointmentResponse updateAppointmentStatus(Long appointmentId, AppointmentStatus newStatus) {

        Appointment appointment = appointmentDomainService.getById(appointmentId);

        // TODO: Status değişiminde event fırlatan bir yapı uygulanacak, layer ? domain or application?
        appointmentDomainService.transitionStatus(appointment, newStatus);

        Appointment updatedAppointment = appointmentDomainService.save(appointment);
        return mapToResponse(updatedAppointment);
    }

    @Override
    @Transactional(readOnly = true)
    public AppointmentResponse getAppointmentById(Long appointmentId) {
        Appointment appointment = appointmentDomainService.getById(appointmentId);
        return mapToResponse(appointment);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<AppointmentResponse> getAppointmentsByUserId(Long userId, Pageable pageable) {
        AppointmentSearchCriteria appointmentSearchCriteria = new AppointmentSearchCriteria(
                userId,
                null,
                null,
                null,
                null,
                null,
                null
        );
        Page<Appointment> appointments = appointmentDomainService.searchAppointments(appointmentSearchCriteria,pageable);
        return appointments.map(this::mapToResponse);
    }

    // TODO: getActiveAppointmentsByUserId(userId) metodu ekle

    @Override
    @Transactional(readOnly = true)
    public Page<AppointmentResponse> getAppointmentsByStaffAndDateRange(Long staffId, LocalDateTime startDate, LocalDateTime endDate, Pageable pageable) {
        AppointmentSearchCriteria appointmentSearchCriteria = new AppointmentSearchCriteria(
                null,
                staffId,
                null,
                null,
                null,
                startDate,
                endDate
        );
        Page<Appointment> appointments = appointmentDomainService.searchAppointments(appointmentSearchCriteria,pageable);
        return appointments.map(this::mapToResponse);
    }

    private AppointmentResponse mapToResponse(Appointment appointment) {
        return new AppointmentResponse(
                appointment.getId(),
                appointment.getStartTime(),
                appointment.getEndTime(),
                appointment.getPrice(),
                appointment.getAppointmentStatus(),
                appointment.getBusiness() != null ? appointment.getBusiness().getId() : null,
                appointment.getBusiness() != null ? appointment.getBusiness().getName() : null,
                appointment.getStaff() != null ? appointment.getStaff().getId() : null,
                appointment.getStaff() != null ? appointment.getStaff().getName() : null,
                appointment.getService() != null ? appointment.getService().getId() : null,
                appointment.getService() != null ? appointment.getService().getName() : null,
                appointment.getCustomerName(),
                appointment.getCustomerPhone()
        );
    }
}
