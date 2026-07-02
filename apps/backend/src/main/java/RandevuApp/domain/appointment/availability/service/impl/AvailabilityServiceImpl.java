package RandevuApp.domain.appointment.availability.service.impl;

import RandevuApp.domain.appointment.availability.model.TimeSlot;
import RandevuApp.domain.appointment.availability.service.IAvailabilityService;
import RandevuApp.domain.appointment.model.Appointment;
import RandevuApp.domain.appointment.model.AppointmentStatus;
import RandevuApp.domain.appointment.repository.AppointmentRepository;
import RandevuApp.domain.business.model.BusinessOperatingHour;
import RandevuApp.domain.business.service.IBusinessScheduleDomainService;
import RandevuApp.domain.time_off.model.TimeOff;
import RandevuApp.domain.time_off.service.ITimeOffDomainService;
import RandevuApp.exceptions.client.BusinessClosedException;
import RandevuApp.exceptions.client.ConflictException;
import RandevuApp.exceptions.client.InvalidInputException;
import RandevuApp.exceptions.client.SlotUnavailableException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class AvailabilityServiceImpl implements IAvailabilityService {

    private final IBusinessScheduleDomainService businessScheduleDomainService;
    private final AppointmentRepository appointmentRepository;
    private final ITimeOffDomainService timeOffDomainService;

    private static final int SLOT_INCREMENT_MINUTES = 30;

    private static final List<AppointmentStatus> BLOCKING_STATUSES = List.of(
            AppointmentStatus.CONFIRMED,
            AppointmentStatus.PENDING
    );

    @Override
    public List<TimeSlot> getAvailableSlotsForStaff(Long businessId, Long staffId, LocalDate date, int serviceDurationMinutes) {

        Optional<BusinessOperatingHour> operatingHourOpt = businessScheduleDomainService.getEffectiveOperatingHours(businessId, date);
        if (operatingHourOpt.isEmpty()) {
            return new ArrayList<>();
        }

        BusinessOperatingHour operatingHour = operatingHourOpt.get();

        if (operatingHour.isClosed()){
            return  new ArrayList<>();
        }

        LocalDateTime openTime = date.atTime(operatingHour.getOpenTime());
        LocalDateTime closeTime = date.atTime(operatingHour.getCloseTime());

        if (operatingHour.isClosed()){
            return new ArrayList<>();
        }

        List<TimeSlot> blockers = getBlockersForStaffAndDate(staffId, openTime, closeTime);

        return calculateGaps(openTime, closeTime, serviceDurationMinutes, blockers);
    }

    private List<TimeSlot> getBlockersForStaffAndDate(Long staffId, LocalDateTime openTime, LocalDateTime closeTime) {
        List<TimeSlot> blockers = new ArrayList<>();

        // TimeOff's
        List<TimeOff> timeOffs = timeOffDomainService.getStaffTimeOffsBetween(staffId, openTime, closeTime);
        for (TimeOff to : timeOffs) {
            blockers.add(new TimeSlot(to.getStartTime(), to.getEndTime()));
        }

        // Appointments
        List<Appointment> appointments = appointmentRepository.findActiveAppointmentsByStaffAndFrame(
                staffId,
                openTime,
                closeTime,
                BLOCKING_STATUSES
        );
        for (Appointment app : appointments) {
            blockers.add(new TimeSlot(app.getStartTime(), app.getEndTime()));
        }

        return blockers;
    }

    private List<TimeSlot> calculateGaps(LocalDateTime openTime, LocalDateTime closeTime, int duration, List<TimeSlot> blockers) {
        List<TimeSlot> availableSlots = new ArrayList<>();
        LocalDateTime currentSlotStart = openTime;

        while (!currentSlotStart.plusMinutes(duration).isAfter(closeTime)) {

            LocalDateTime currentSlotEnd = currentSlotStart.plusMinutes(duration);

            if (!isOverlapping(currentSlotStart, currentSlotEnd, blockers)) {
                availableSlots.add(new TimeSlot(currentSlotStart, currentSlotEnd));
            }

            currentSlotStart = currentSlotStart.plusMinutes(SLOT_INCREMENT_MINUTES);
        }

        return availableSlots;
    }

    private boolean isOverlapping(LocalDateTime start, LocalDateTime end, List<TimeSlot> blockers) {
        for (TimeSlot blocker : blockers) {
            if (start.isBefore(blocker.endTime()) && end.isAfter(blocker.startTime())) {
                return true;
            }
        }
        return false;
    }


    @Override
    public Map<LocalDate, List<TimeSlot>> getAvailableSlotsForDateRange(Long businessId, Long staffId, LocalDate startDate, LocalDate endDate, int serviceDurationMinutes) {
        Map<LocalDate, List<TimeSlot>> weeklyAvailability = new LinkedHashMap<>();

        if (startDate.isAfter(endDate)) {
            throw new InvalidInputException("Başlangıç tarihi bitiş tarihinden sonra olamaz.");
        }

        LocalDate currentDate = startDate;
        while (!currentDate.isAfter(endDate)) {
            List<TimeSlot> dailySlots = getAvailableSlotsForStaff(businessId, staffId, currentDate, serviceDurationMinutes);
            weeklyAvailability.put(currentDate, dailySlots);
            currentDate = currentDate.plusDays(1);
        }

        return weeklyAvailability;
    }

    @Override
    public void validateSlotAvailability(Long businessId, Long staffId, LocalDateTime requestedStartTime, LocalDateTime requestedEndTime, Long excludeAppointmentId) {

        if (!requestedStartTime.isBefore(requestedEndTime)) {
            throw new InvalidInputException("Randevu bitiş zamanı, başlangıç zamanından sonra olmalıdır.");
        }

        LocalDate requestedDate = requestedStartTime.toLocalDate();

        Optional<BusinessOperatingHour> operatingHourOpt = businessScheduleDomainService.getEffectiveOperatingHours(businessId, requestedDate);

        if (operatingHourOpt.isEmpty() || operatingHourOpt.get().isClosed()) {
            throw new BusinessClosedException("İşletme bu tarihte kapalıdır.");
        }

        BusinessOperatingHour operatingHour = operatingHourOpt.get();
        LocalDateTime businessOpenTime = requestedDate.atTime(operatingHour.getOpenTime());
        LocalDateTime businessCloseTime = requestedDate.atTime(operatingHour.getCloseTime());

        if (requestedStartTime.isBefore(businessOpenTime) || requestedEndTime.isAfter(businessCloseTime)) {
            throw new SlotUnavailableException("Talep edilen saatler işletmenin çalışma saatleri dışındadır.");
        }

        List<TimeOff> timeOffs = timeOffDomainService.getStaffTimeOffsBetween(staffId, requestedStartTime, requestedEndTime);
        if (!timeOffs.isEmpty()) {
            throw new SlotUnavailableException("Seçilen saatlerde personelin izni veya molası bulunmaktadır.");
        }

        List<Appointment> overlappingAppointments = appointmentRepository.findConflictingAppointments(
                staffId,
                requestedStartTime,
                requestedEndTime,
                BLOCKING_STATUSES
        );

        for (Appointment appointment : overlappingAppointments) {
            // Reschedule case: if conflicted appointment is a rescheduling appointment, exclude it.
            if (excludeAppointmentId != null && appointment.getId().equals(excludeAppointmentId)) {
                continue;
            }
            throw new ConflictException("Seçilen saatler başka bir müşterinin randevusu ile çakışmaktadır.");
        }
    }
}
