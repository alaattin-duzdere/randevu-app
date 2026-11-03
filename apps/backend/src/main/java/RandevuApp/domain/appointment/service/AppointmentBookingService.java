package RandevuApp.domain.appointment.service;

import RandevuApp.commons.util.TimeConverter;
import RandevuApp.commons.util.TimeZoneUtil;
import RandevuApp.config.BusinessProperties;
import RandevuApp.domain.appointment.availability.service.IAvailabilityDomainService;
import RandevuApp.domain.appointment.dto.CreateAppointmentCommand;
import RandevuApp.domain.appointment.model.Appointment;
import RandevuApp.domain.appointment.service.params.CreateAppointmentParams;
import RandevuApp.domain.business.model.Business;
import RandevuApp.domain.business.service.impl.BusinessDomainService;
import RandevuApp.domain.service_offering.model.ServiceOffering;
import RandevuApp.domain.service_offering.service.IServiceOfferingDomainService;
import RandevuApp.domain.staff.model.Staff;
import RandevuApp.domain.staff.service.IStaffDomainService;
import RandevuApp.domain.user.model.User;
import RandevuApp.domain.user.service.IUserDomainService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.ZoneId;

@Slf4j
@Service
@RequiredArgsConstructor
public class AppointmentBookingService {

    private final IUserDomainService userDomainService;
    private final BusinessDomainService businessDomainService;
    private final IStaffDomainService staffDomainService;
    private final IServiceOfferingDomainService serviceOfferingDomainService;
    private final IAppointmentDomainService appointmentDomainService;
    private final IAvailabilityDomainService availabilityService;
    private final TimeConverter timeConverter;
    private final BusinessProperties businessProperties;

    @Transactional
    public Appointment book(CreateAppointmentCommand command) {
        User user = userDomainService.findUserById(command.userId());
        Business business = businessDomainService.getById(command.businessId());
        Staff staff = staffDomainService.getByIdAndBusinessId(command.staffId(), command.businessId());
        ServiceOffering serviceOffering = serviceOfferingDomainService.getById(command.serviceId());

        ZoneId zoneId = getBusinessZoneId(business);
        LocalDateTime localStartTime = timeConverter.toLocal(command.startTime(), zoneId);
        LocalDateTime endTime = localStartTime.plusMinutes(serviceOffering.getDurationInMinutes());

        availabilityService.validateSlotAvailability(
                business.getId(), staff.getId(), localStartTime, endTime, null);

        CreateAppointmentParams params = CreateAppointmentParams.builder()
                .user(user)
                .business(business)
                .staff(staff)
                .service(serviceOffering)
                .startTime(localStartTime)
                .endTime(endTime)
                .customerName(user.getFirstName())
                .customerLastName(user.getLastName())
                .customerPhone(user.getPhoneNumber())
                .timeZone(business.getTimeZone())
                .build();

        return appointmentDomainService.save(appointmentDomainService.createEntity(params));
    }

    private ZoneId getBusinessZoneId(Business business) {
        String tz = business.getTimeZone();
        ZoneId zoneId = TimeZoneUtil.resolve(tz, businessProperties.getDefaultTimezone());
        if (!zoneId.getId().equals(tz) && TimeZoneUtil.isValid(tz)) {
            log.warn("Invalid or missing timezone '{}' configured for business ID {}. Falling back to default.", tz, business.getId());
        }
        return zoneId;
    }
}
