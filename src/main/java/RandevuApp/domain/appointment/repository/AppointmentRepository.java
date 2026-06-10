package RandevuApp.domain.appointment.repository;

import RandevuApp.domain.appointment.model.Appointment;
import RandevuApp.domain.appointment.model.AppointmentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment,Long>, JpaSpecificationExecutor<Appointment> {

    boolean existsByBusinessIdAndAppointmentStatusIn(Long businessId, List<AppointmentStatus> statuses);

    boolean existsByUserIdAndAppointmentStatusIn(Long userId, List<AppointmentStatus> statuses);

    @Query("""
           SELECT a FROM Appointment a 
           WHERE a.staff.id = :staffId 
           AND a.appointmentStatus IN :activeStatuses 
           AND a.startTime < :openTime 
           AND a.endTime > :closeTime
           """)
    List<Appointment> findActiveAppointmentsByStaffAndFrame(
            @Param("staffId") Long staffId,
            @Param("openTime") LocalDateTime openTime,
            @Param("closeTime") LocalDateTime closeTime,
            @Param("activeStatuses") List<AppointmentStatus> activeStatuses
    );

    @Query("""
           SELECT a FROM Appointment a 
           WHERE a.staff.id = :staffId 
           AND a.appointmentStatus IN :activeStatuses 
           AND a.startTime < :requestedEndTime 
           AND a.endTime > :requestedStartTime
           """)
    List<Appointment> findConflictingAppointments(
            @Param("staffId") Long staffId,
            @Param("requestedStartTime") LocalDateTime requestedStartTime,
            @Param("requestedEndTime") LocalDateTime requestedEndTime,
            @Param("activeStatuses") List<AppointmentStatus> activeStatuses
    );
}
