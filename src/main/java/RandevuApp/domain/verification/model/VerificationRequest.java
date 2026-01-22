package RandevuApp.domain.verification.model;

import RandevuApp.domain.notification.model.NotificationChannel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class VerificationRequest {
    private Long userId;
    private VerificationType type;
    private NotificationChannel channel;
    private VerificationPurpose purpose;
}
