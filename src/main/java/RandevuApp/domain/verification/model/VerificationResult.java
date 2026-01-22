package RandevuApp.domain.verification.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.Map;

@Getter
@Setter
@AllArgsConstructor
public class VerificationResult {
    private Long userId;
    private VerificationPurpose purpose;
    private Instant verifiedAt;
    private Map<String, Object> metadata;
}
