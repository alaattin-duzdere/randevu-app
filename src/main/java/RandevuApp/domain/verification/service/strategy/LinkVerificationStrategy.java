package RandevuApp.domain.verification.service.strategy;

import RandevuApp.domain.notification.model.NotificationChannel;
import RandevuApp.domain.verification.model.NotificationPayload;
import RandevuApp.domain.verification.model.VerificationPurpose;
import RandevuApp.domain.verification.model.VerificationType;
import RandevuApp.exceptions.verification.VerificationFailedException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class LinkVerificationStrategy implements IVerificationStrategy {

    @Value("${app.base-url}")
    private String baseUrl;

    @Override
    public VerificationType getSupportedType() {
        return VerificationType.LINK;
    }

    @Override
    public String generateSecret() {
        return java.util.UUID.randomUUID().toString(); // Token
    }

    @Override
    public NotificationPayload prepareNotification(Long userId, String secret, NotificationChannel channel, VerificationPurpose purpose) {
        String verificationUrl = baseUrl + "/api/v1/verification/confirm-link?token=" + secret + "&userId=" + userId + "&purpose=" + purpose;
        Map<String, Object> variables = Map.of("link", verificationUrl);
        String subject = "Hesabınızı Doğrulayın";
        String message;

        if (channel == NotificationChannel.EMAIL) {
            message = "Hesabınızı doğrulamak için lütfen e-postadaki bağlantıya tıklayın.";
        } else {
            message = "Hesabınızı doğrulamak için lütfen bağlantıya tıklayın: " + verificationUrl;
        }
        return new NotificationPayload(subject, message, variables);
    }

    @Override
    public void validate(String storedSecret, String inputSecret, PasswordEncoder passwordEncoder) {
        if (!passwordEncoder.matches(inputSecret, storedSecret)) {
            throw new VerificationFailedException("Geçersiz veya süresi dolmuş Link!");
        }
    }
}
