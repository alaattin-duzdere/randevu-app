package RandevuApp.domain.verification.service.strategy;

import RandevuApp.domain.notification.model.NotificationChannel;
import RandevuApp.domain.verification.model.NotificationPayload;
import RandevuApp.domain.verification.model.VerificationPurpose;
import RandevuApp.domain.verification.model.VerificationType;
import org.springframework.security.crypto.password.PasswordEncoder;

public interface IVerificationStrategy {
    VerificationType getSupportedType();

    String generateSecret();

    NotificationPayload prepareNotification(String referenceId, String secret, NotificationChannel channel, VerificationPurpose purpose);

    void validate(String storedSecret, String inputSecret, PasswordEncoder passwordEncoder);
}
