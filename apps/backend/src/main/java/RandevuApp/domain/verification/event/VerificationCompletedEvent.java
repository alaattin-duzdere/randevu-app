package RandevuApp.domain.verification.event;

import RandevuApp.domain.verification.model.VerificationPurpose;
import lombok.Getter;

import java.time.Instant;
import java.util.Map;

@Getter
public class VerificationCompletedEvent {
    private final Long userId;
    private final VerificationPurpose purpose;
    private final String referenceId;
    private final Map<String, Object> metadata;
    private final Instant completedAt;

    public VerificationCompletedEvent(Long userId, VerificationPurpose purpose, String referenceId, Map<String, Object> metadata) {
        this.userId = userId;
        this.purpose = purpose;
        this.referenceId = referenceId;
        this.metadata = metadata;
        this.completedAt = Instant.now();
    }
}