package RandevuApp.domain.verification.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import java.time.Instant;
import java.util.Map;
import java.util.UUID;

@Entity
@Table(name = "verification_tokens", indexes = {
        @Index(name = "idx_verification_user_type_purpose", columnList = "user_id, verification_type, purpose"), // Amaç bazlı arama için güncellendi
        @Index(name = "idx_verification_secret", columnList = "secret"), // Link ile token aramak için
        @Index(name = "idx_verification_expires", columnList = "expires_at"), // Süresi dolanları temizlemek için
        @Index(name = "idx_verification_reference", columnList = "reference_id") // Referans ile arama için
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VerificationEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "reference_id", nullable = false)
    private String referenceId;

    @Column(name = "user_id", nullable = true)
    private Long userId;

    @Enumerated(EnumType.STRING)
    @Column(name = "verification_type", nullable = false)
    private VerificationType type;

    @Enumerated(EnumType.STRING)
    @Column(name = "purpose", nullable = false)
    @Builder.Default
    private VerificationPurpose purpose = VerificationPurpose.GENERAL;

    @Column(nullable = false)
    private String secret;

    @Column(name = "expires_at", nullable = false)
    private Instant expiresAt;

    @Column(name = "confirmed_at")
    private Instant confirmedAt;

    @Builder.Default
    @Column(name = "attempt_count")
    private int attemptCount = 0;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "jsonb")
    private Map<String, Object> metadata;

    public boolean isExpired() {
        return Instant.now().isAfter(this.expiresAt);
    }

    public boolean isMaxAttemptsReached(int maxAttempts) {
        return this.attemptCount >= maxAttempts;
    }
}
