package RandevuApp.domain.verification.repository;

import RandevuApp.domain.verification.model.VerificationEntity;
import RandevuApp.domain.verification.model.VerificationPurpose;
import RandevuApp.domain.verification.model.VerificationType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface VerificationTokenRepository extends JpaRepository<VerificationEntity, UUID> {

    void deleteByExpiresAtBefore(LocalDateTime cutOffTime);

    Optional<VerificationEntity> findTopByUserIdAndTypeAndPurposeAndConfirmedAtIsNullOrderByExpiresAtDesc(Long userId, VerificationType type, VerificationPurpose purpose);

    Optional<VerificationEntity> findTopByReferenceIdAndTypeAndPurposeAndConfirmedAtIsNullOrderByExpiresAtDesc(String referenceId, VerificationType type, VerificationPurpose purpose);

    // Old zombies delete when new token come
    void deleteByUserIdAndTypeAndPurposeAndConfirmedAtIsNull(Long userId, VerificationType type, VerificationPurpose purpose);
    void deleteByReferenceIdAndTypeAndPurposeAndConfirmedAtIsNull(String referenceId, VerificationType type, VerificationPurpose purpose);

    // super duper amazing fantastic query
    @Modifying(clearAutomatically = true)
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @Query("UPDATE VerificationEntity v SET v.attemptCount = v.attemptCount + 1 WHERE v.id = :id")
    void incrementAttemptCount(@Param("id") UUID id);
}
