
package RandevuApp.domain.time_off.repository;

import RandevuApp.domain.time_off.model.TimeOff;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface TimeOffRepository extends JpaRepository<TimeOff, Long> {

    Optional<TimeOff> findByIdAndStaffId(Long id, Long staffId);

    List<TimeOff> findAllByStaffId(Long staffId);

    // 1. ÇAKIŞMA KONTROLÜ (Overlap Check)
    // Eğer mevcut kaydın başlangıcı, istenen bitişten önceyse VE mevcut kaydın bitişi, istenen başlangıçtan sonraysa çakışma vardır!
    @Query("SELECT COUNT(t) > 0 FROM TimeOff t WHERE t.staff.id = :staffId " +
            "AND t.startTime < :endTime AND t.endTime > :startTime " +
            "AND (:excludeTimeOffId IS NULL OR t.id != :excludeTimeOffId)")
    boolean existsOverlappingTimeOff(@Param("staffId") Long staffId,
                                     @Param("startTime") LocalDateTime startTime,
                                     @Param("endTime") LocalDateTime endTime,
                                     @Param("excludeTimeOffId") Long excludeTimeOffId);

    // 2. BELİRLİ TARİH ARALIĞINDAKİ İZİNLERİ GETİRME (Takvim için)
    @Query("SELECT t FROM TimeOff t WHERE t.staff.id = :staffId " +
            "AND t.startTime < :endDate AND t.endTime > :startDate " +
            "ORDER BY t.startTime ASC")
    List<TimeOff> findOverlappingWithRange(@Param("staffId") Long staffId,
                                           @Param("startDate") LocalDateTime startDate,
                                           @Param("endDate") LocalDateTime endDate);
}