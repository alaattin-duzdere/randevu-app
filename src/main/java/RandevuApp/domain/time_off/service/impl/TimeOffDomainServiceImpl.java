package RandevuApp.domain.time_off.service.impl;

import RandevuApp.domain.staff.model.Staff;
import RandevuApp.domain.time_off.model.TimeOff;
import RandevuApp.domain.time_off.model.TimeOffType;
import RandevuApp.domain.time_off.repository.TimeOffRepository;
import RandevuApp.domain.time_off.service.ITimeOffDomainService;
import RandevuApp.exceptions.client.ConflictException;
import RandevuApp.exceptions.client.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TimeOffDomainServiceImpl implements ITimeOffDomainService {

    private final TimeOffRepository timeOffRepository;

    // --- FACTORY ---

    @Override
    public TimeOff createEntity(Staff staff, LocalDateTime startTime, LocalDateTime endTime, TimeOffType type, String note) {
        return TimeOff.builder()
                .staff(staff)
                .startTime(startTime)
                .endTime(endTime)
                .type(type)
                .note(note)
                .build();
    }

    // --- BUSINESS RULES ---

    @Override
    public void validateDates(LocalDateTime startTime, LocalDateTime endTime) {
        if (endTime.isBefore(startTime) || endTime.isEqual(startTime)) {
            throw new IllegalArgumentException("İzin bitiş zamanı, başlangıç zamanından sonra olmalıdır.");
        }
    }

    @Override
    public void validateNoOverlap(Long staffId, LocalDateTime startTime, LocalDateTime endTime, Long excludeTimeOffId) {
        boolean hasOverlap = timeOffRepository.existsOverlappingTimeOff(staffId, startTime, endTime, excludeTimeOffId);

        if (hasOverlap) {
            throw new ConflictException("Personelin bu saatler arasında başka bir izni veya molası bulunuyor. Lütfen saatleri kontrol edin.");
        }
    }

    // --- REPOSITORY WRAPPERS ---

    @Override
    public TimeOff save(TimeOff timeOff) {
        return timeOffRepository.save(timeOff);
    }

    @Override
    public TimeOff getByIdAndStaffId(Long timeOffId, Long staffId) {
        return timeOffRepository.findByIdAndStaffId(timeOffId, staffId)
                .orElseThrow(() -> new ResourceNotFoundException("İzin kaydı bulunamadı veya bu personele ait değil", "id", timeOffId));
    }

    @Override
    public List<TimeOff> getAllByStaffId(Long staffId) {
        return timeOffRepository.findAllByStaffId(staffId);
    }

    @Override
    public List<TimeOff> getStaffTimeOffsBetween(Long staffId, LocalDateTime startDate, LocalDateTime endDate) {
        if (endDate.isBefore(startDate)) {
            throw new IllegalArgumentException("Arama bitiş tarihi, başlangıç tarihinden önce olamaz.");
        }
        return timeOffRepository.findOverlappingWithRange(staffId, startDate, endDate);
    }

    @Override
    public void delete(TimeOff timeOff) {
        timeOffRepository.delete(timeOff);
    }
}