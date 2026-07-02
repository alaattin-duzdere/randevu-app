package RandevuApp.domain.appointment.repository;

import RandevuApp.domain.appointment.model.Appointment;
import RandevuApp.domain.appointment.model.AppointmentStatus;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;
import java.util.List;

public class AppointmentSpecs {

    private AppointmentSpecs() {}

    public static Specification<Appointment> hasUserId(Long userId) {
        return (root, query, cb) -> cb.equal(root.get("user").get("id"), userId);
    }

    public static Specification<Appointment> hasStaffId(Long staffId) {
        return (root, query, cb) -> cb.equal(root.get("staff").get("id"), staffId);
    }

    public static Specification<Appointment> hasBusinessId(Long businessId) {
        return (root, query, cb) -> cb.equal(root.get("business").get("id"), businessId);
    }

    public static Specification<Appointment> hasServiceId(Long serviceId) {
        return (root, query, cb) -> cb.equal(root.get("service").get("id"), serviceId);
    }

    public static Specification<Appointment> hasStatuses(List<AppointmentStatus> statuses) {
        return (root, query, cb) -> root.get("appointmentStatus").in(statuses);
    }

    public static Specification<Appointment> isAfterOrEqual(LocalDateTime startDate) {
        return (root, query, cb) -> cb.greaterThanOrEqualTo(root.get("startTime"), startDate);
    }

    public static Specification<Appointment> isBeforeOrEqual(LocalDateTime endDate) {
        return (root, query, cb) -> cb.lessThanOrEqualTo(root.get("startTime"), endDate);
    }
}
