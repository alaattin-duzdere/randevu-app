package RandevuApp.domain.verification.service;

import RandevuApp.api.ApiStatus;
import RandevuApp.config.VerificationProperties;
import RandevuApp.domain.notification.model.NotificationCategory;
import RandevuApp.domain.notification.model.VerificationNotificationRequest;
import RandevuApp.domain.notification.model.NotificationChannel;
import RandevuApp.domain.notification.service.INotificationService;
import RandevuApp.domain.user.model.User;
import RandevuApp.domain.user.repository.UserRepository;
import RandevuApp.domain.verification.event.VerificationCompletedEvent;
import RandevuApp.domain.verification.model.*;
import RandevuApp.domain.verification.repository.VerificationTokenRepository;
import RandevuApp.domain.verification.service.strategy.IVerificationStrategy;
import RandevuApp.exceptions.client.ResourceNotFoundException;
import RandevuApp.exceptions.verification.VerificationExpiredException;
import RandevuApp.exceptions.verification.VerificationFailedException;
import RandevuApp.exceptions.verification.VerificationNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@Service
public class VerificationService {

    private final Map<VerificationType, IVerificationStrategy> strategyMap;

    private final INotificationService notificationService;
    private final VerificationTokenRepository tokenRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final ApplicationEventPublisher eventPublisher;
    private final VerificationProperties verificationProperties;

    public VerificationService(List<IVerificationStrategy> strategies,
                               INotificationService notificationService,
                               VerificationTokenRepository tokenRepository, UserRepository userRepository, PasswordEncoder passwordEncoder, ApplicationEventPublisher eventPublisher, VerificationProperties verificationProperties) {
        this.notificationService = notificationService;
        this.tokenRepository = tokenRepository;
        // List to Map conversion for easy strategy lookup in constructor
        this.strategyMap = strategies.stream()
                .collect(Collectors.toMap(IVerificationStrategy::getSupportedType, Function.identity()));
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.eventPublisher = eventPublisher;
        this.verificationProperties = verificationProperties;
    }

    @Transactional
    public void startVerification(VerificationRequest request) {
        IVerificationStrategy strategy = strategyMap.get(request.getType());

        log.warn("Verification Strategy: {}",strategy.toString());

        // Find user
        User user = userRepository.findById(request.getUserId()).orElseThrow(() -> new ResourceNotFoundException("userId", "id", request.getUserId()));

        // Generate secret and encode it
        String rawSecret = strategy.generateSecret();
        String encodedSecret = passwordEncoder.encode(rawSecret);

        // Resolve purpose
        VerificationPurpose purpose = request.getPurpose() != null ? request.getPurpose() : VerificationPurpose.GENERAL;

        // Clean up old unconfirmed tokens (zombies) for this specific flow
        tokenRepository.deleteByUserIdAndTypeAndPurposeAndConfirmedAtIsNull(request.getUserId(), request.getType(), purpose);

        // Resolve Reference ID (If specific ID provided use it, otherwise default to userId)
        String referenceId = request.getReferenceId() != null ? request.getReferenceId() : String.valueOf(request.getUserId());

        // Create and save token
        VerificationEntity entity = new VerificationEntity();
        entity.setUserId(request.getUserId());
        entity.setSecret(encodedSecret);
        entity.setType(request.getType());
        entity.setPurpose(purpose);
        entity.setReferenceId(referenceId);
        entity.setExpiresAt(Instant.now().plus(verificationProperties.getTokenValidityMinutes(), ChronoUnit.MINUTES));
        tokenRepository.save(entity);

        // Prepare notification
        NotificationPayload payload = strategy.prepareNotification(request.getUserId(), rawSecret, request.getChannel(), purpose);
        String recipient = resolveRecipient(request.getChannel(), user);

        // Determine category based on verification type
        NotificationCategory category = (request.getType() == VerificationType.LINK) 
                ? NotificationCategory.LINK_VERIFICATION 
                : NotificationCategory.CODE_VERIFICATION;

        VerificationNotificationRequest verificationNotificationRequest = VerificationNotificationRequest.builder()
                .userId(request.getUserId())
                .recipient(recipient)
                .channel(request.getChannel())
                .message(payload.getMessage())
                .subject(payload.getSubject())
                .category(category) // Set dynamic category
                .variables(payload.getVariables())
                .build();

        notificationService.sendVerificationNotification(verificationNotificationRequest);
    }

    private String resolveRecipient(NotificationChannel channel,User user) {
        if (channel == NotificationChannel.EMAIL) {
            return user.getEmail();
        }
        return user.getPhoneNumber();
    }

    @Transactional
    public VerificationResult verify(String input, VerificationType type, Long userId, VerificationPurpose purpose) {
        IVerificationStrategy strategy = strategyMap.get(type);
        VerificationPurpose searchPurpose = (purpose != null) ? purpose : VerificationPurpose.GENERAL;

        // Find PENDING (unconfirmed) token by userId, verification type AND purpose
        VerificationEntity entity = tokenRepository.findTopByUserIdAndTypeAndPurposeAndConfirmedAtIsNullOrderByExpiresAtDesc(userId, type, searchPurpose)
                .orElseThrow(() -> new VerificationNotFoundException("Doğrulama kaydı bulunamadı."));

        log.warn("Founded verification entity: {}",entity.getId());

        // Check if token is expired
        if (entity.getExpiresAt().isBefore(Instant.now())) {
            throw new VerificationExpiredException("Kod süresi dolmuş. Lütfen yeni kod isteyin.");
        }

        // Check attempt count
        if (entity.isMaxAttemptsReached(verificationProperties.getMaxAttempts())) {
            throw new VerificationFailedException(ApiStatus.ERROR_TOO_MANY_ATTEMPTS, "Çok fazla hatalı deneme yapıldı. Lütfen yeni kod isteyin.");
        }

        try {
            strategy.validate(entity.getSecret(), input, passwordEncoder);
        } catch (Exception e) {
            // Increment attempt count on failure
            tokenRepository.incrementAttemptCount(entity.getId());
            throw e;
        }

        // If token valid mark as confirmed
        entity.setConfirmedAt(Instant.now());
        tokenRepository.save(entity);

        // Publish event (Side flow)
        VerificationCompletedEvent event = new VerificationCompletedEvent(
                entity.getUserId(),
                entity.getPurpose(),
                entity.getReferenceId(),
                entity.getMetadata()
        );
        eventPublisher.publishEvent(event);

        // Return result(for main flow)
        return new VerificationResult(
                entity.getUserId(),
                entity.getPurpose(),
                entity.getConfirmedAt(),
                entity.getMetadata()
        );
    }
}
