package RandevuApp.domain.user.listener;

import RandevuApp.domain.user.model.User;
import RandevuApp.domain.user.model.UserStatus;
import RandevuApp.domain.user.repository.UserRepository;
import RandevuApp.domain.verification.event.VerificationCompletedEvent;
import RandevuApp.exceptions.client.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

@Slf4j
@Component
@RequiredArgsConstructor
public class UserVerificationListener {

    private final UserRepository userRepository;

    @EventListener(condition = "#event.purpose.name() == 'REGISTRATION'")
    @Transactional
    public void onRegistrationVerification(VerificationCompletedEvent event) {
        log.info("Handling REGISTRATION verification for User ID: {}", event.getUserId());

        User user = getUser(event.getUserId());

        user.setPhoneVerifiedAt(Instant.now());
        user.setStatus(UserStatus.ACTIVE);
        
        userRepository.save(user);
        log.info("User ID: {} activated and phone verified successfully.", user.getId());
    }

    @EventListener(condition = "#event.purpose.name() == 'PHONE_VERIFICATION'")
    @Transactional
    public void onPhoneVerification(VerificationCompletedEvent event) {
        log.info("Handling PHONE_VERIFICATION for User ID: {}", event.getUserId());

        User user = getUser(event.getUserId());

        user.setPhoneVerifiedAt(Instant.now());
        
        userRepository.save(user);
        log.info("User ID: {} phone number verified successfully.", user.getId());
    }

    @EventListener(condition = "#event.purpose.name() == 'EMAIL_VERIFICATION'")
    @Transactional
    public void onEmailVerification(VerificationCompletedEvent event) {
        log.info("Handling EMAIL_VERIFICATION for User ID: {}", event.getUserId());

        User user = getUser(event.getUserId());

        user.setEmailVerifiedAt(Instant.now());
        
        userRepository.save(user);
        log.info("User ID: {} email address verified successfully.", user.getId());
    }

    // Helper method to avoid code duplication
    private User getUser(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));
    }
}
