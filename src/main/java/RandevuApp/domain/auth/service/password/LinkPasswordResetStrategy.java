package RandevuApp.domain.auth.service.password;

import RandevuApp.config.VerificationProperties;
import RandevuApp.domain.auth.model.PasswordResetToken;
import RandevuApp.domain.auth.repository.PasswordResetTokenRepository;
import RandevuApp.domain.notification.model.NotificationCategory;
import RandevuApp.domain.notification.model.NotificationChannel;
import RandevuApp.domain.notification.model.NotificationRequest;
import RandevuApp.domain.notification.model.VerificationNotificationRequest;
import RandevuApp.domain.notification.service.INotificationService;
import RandevuApp.domain.user.model.User;
import RandevuApp.domain.user.repository.UserRepository;
import RandevuApp.exceptions.client.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

@Slf4j
@Service("linkPasswordReset")
@RequiredArgsConstructor
public class LinkPasswordResetStrategy implements IPasswordResetStrategy {

    private final UserRepository userRepository;
    private final PasswordResetTokenRepository tokenRepository;
    private final INotificationService notificationService;
    private final VerificationProperties verificationProperties;

    @Value("${app.frontend-url}")
    private String frontendUrl;

    @Override
    @Transactional
    public String sendResetToken(String email) {
        log.info("Creating password reset token for email: {}", email);
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User", "email", email));

        // Eski tokenları temizle
        tokenRepository.deleteByUser(user);

        // Yeni token oluştur
        String token = UUID.randomUUID().toString();
        PasswordResetToken resetToken = PasswordResetToken.builder()
                .token(token)
                .user(user)
                .expiryDate(Instant.now().plus(verificationProperties.getPasswordResetTokenValidityMinutes(), ChronoUnit.MINUTES))
                .build();
        
        tokenRepository.save(resetToken);

        log.warn("Sending password reset email to: {}", email);
        String subject = "Reset your password";
        String url = frontendUrl + "/reset-password?token=" + token;
        String message = "Click the link to reset your password: " + url;

        // NotificationRequest oluştur
        VerificationNotificationRequest verificationNotificationRequest = VerificationNotificationRequest.builder()
                .recipient(email)
                .userId(user.getId())
                .subject(subject)
                .message(message)
                .category(NotificationCategory.PASSWORD_RESET)
                .variable("link", url)
                .variable("name", user.getFirstName())
                .channel(NotificationChannel.EMAIL)
                .build();

        notificationService.sendVerificationNotification(verificationNotificationRequest);

        return "Password reset instructions sent to " + email;
    }
}
