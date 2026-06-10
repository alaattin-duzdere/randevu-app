package RandevuApp.domain.auth.service.password;

import RandevuApp.config.VerificationProperties;
import RandevuApp.domain.auth.model.PasswordResetToken;
import RandevuApp.domain.auth.repository.PasswordResetTokenRepository;
import RandevuApp.domain.notification.model.NotificationCategory;
import RandevuApp.domain.notification.model.NotificationChannel;
import RandevuApp.domain.notification.model.NotificationRequest;
import RandevuApp.domain.notification.service.INotificationService;
import RandevuApp.domain.user.model.User;
import RandevuApp.domain.user.repository.UserRepository;
import RandevuApp.exceptions.client.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

@Slf4j
@Service("smsPasswordReset")
@RequiredArgsConstructor
public class SmsPasswordResetStrategy implements IPasswordResetStrategy {

    private final UserRepository userRepository;
    private final PasswordResetTokenRepository tokenRepository;
    private final INotificationService notificationService;
    private final VerificationProperties verificationProperties;

    @Override
    @Transactional
    public String sendResetToken(String phoneNumber) {
        log.info("Initiating password reset flow (SMS) for phone: {}", phoneNumber);

        User user = userRepository.findByPhoneNumber(phoneNumber)
                .orElseThrow(() -> new ResourceNotFoundException("User", "phone", phoneNumber));

        // Eski tokenları temizle
        tokenRepository.deleteByUser(user);

        // 6 haneli kod üret
        String code = generateCode();
        
        PasswordResetToken resetToken = PasswordResetToken.builder()
                .token(code)
                .user(user)
                .expiryDate(Instant.now().plus(verificationProperties.getPasswordResetTokenValidityMinutes(), ChronoUnit.MINUTES))
                .build();

        tokenRepository.save(resetToken);

        log.warn("Sending password reset SMS to: {}", phoneNumber);
        String message = "Randevu App şifre sıfırlama kodunuz: " + code + ". Bu kod 15 dakika geçerlidir.";

        NotificationRequest notificationRequest = NotificationRequest.builder()
                .recipient(phoneNumber)
                .userId(user.getId())
                .message(message)
                .category(NotificationCategory.PASSWORD_RESET) // SMS için template kullanılmayabilir ama kategori tutarlılığı için iyi
                .explicitChannels(Set.of(NotificationChannel.SMS)) // Sadece SMS kanalından zorunlu gönderim
                .build();

        notificationService.send(notificationRequest);

        return "Password reset code sent to " + phoneNumber;
    }

    private String generateCode() {
        int length = verificationProperties.getCodeLength();
        long min = (long) Math.pow(10, length - 1);
        long max = (long) Math.pow(10, length) - 1;
        return String.valueOf(ThreadLocalRandom.current().nextLong(min, max + 1));
    }
}
