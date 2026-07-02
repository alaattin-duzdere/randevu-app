package RandevuApp.domain.verification.model;

import RandevuApp.domain.notification.model.NotificationChannel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class VerificationRequest {
    private Long userId;
    private VerificationType type;
    private NotificationChannel channel;
    private VerificationPurpose purpose;
    private String referenceId;
}
