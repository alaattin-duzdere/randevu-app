package RandevuApp.domain.appointment.service.params;

import RandevuApp.domain.business.model.Business;
import RandevuApp.domain.service_offering.model.ServiceOffering;
import RandevuApp.domain.staff.model.Staff;
import RandevuApp.domain.user.model.User;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record CreateAppointmentParams(
        User user,
        Business business,
        Staff staff,
        ServiceOffering service,
        LocalDateTime startTime,
        LocalDateTime endTime,
        String customerName,
        String customerLastName,
        String customerPhone,
        String timeZone
) {
}
