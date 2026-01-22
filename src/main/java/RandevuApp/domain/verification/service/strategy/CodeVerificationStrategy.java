package RandevuApp.domain.verification.service.strategy;

import RandevuApp.domain.notification.model.NotificationChannel;
import RandevuApp.domain.verification.model.NotificationPayload;
import RandevuApp.domain.verification.model.VerificationPurpose;
import RandevuApp.domain.verification.model.VerificationType;
import RandevuApp.exceptions.verification.VerificationFailedException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class CodeVerificationStrategy implements IVerificationStrategy{

    @Override
    public VerificationType getSupportedType() {
        return VerificationType.CODE;
    }

    @Override
    public String generateSecret() {
        return String.valueOf((int) ((Math.random() * 900000) + 100000));
    }

    @Override
    public NotificationPayload prepareNotification(Long userId, String secret, NotificationChannel channel, VerificationPurpose purpose) {
        Map<String, Object> variables = Map.of("code", secret);

        if (channel == NotificationChannel.EMAIL) {
            return new NotificationPayload(
                    "Doğrulama Kodunuz",
                    "HTML_TEMPLATE_PATH",
                    variables
            );
        } else {
            return new NotificationPayload(
                    "SMS Doğrulama",
                    "Kodunuz: " + secret,
                    variables
            );
        }
    }

    @Override
    public void validate(String storedSecret, String inputSecret, PasswordEncoder passwordEncoder) {
        if (!passwordEncoder.matches(inputSecret, storedSecret)) {
            throw new VerificationFailedException("Geçersiz Kod!");
        }
    }
}
