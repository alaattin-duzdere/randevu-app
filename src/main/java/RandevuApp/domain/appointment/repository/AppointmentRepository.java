package RandevuApp.domain.appointment.repository;

import RandevuApp.domain.appointment.model.Appointment;
import RandevuApp.domain.appointment.model.AppointmentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment,Long> {

    boolean existsByBusinessIdAndAppointmentStatusIn(Long businessId, List<AppointmentStatus> statuses);

    boolean existsByUserIdAndAppointmentStatusIn(Long userId, List<AppointmentStatus> statuses);
}
