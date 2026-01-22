package RandevuApp.domain.notification.service.impl;

import RandevuApp.domain.notification.model.NotificationCategory;
import RandevuApp.domain.notification.model.NotificationChannel;
import RandevuApp.domain.notification.model.NotificationRequest;
import RandevuApp.domain.notification.model.VerificationNotificationRequest;
import RandevuApp.domain.notification.service.INotificationService;
import RandevuApp.domain.notification.service.IUserNotificationPreferenceService;
import RandevuApp.domain.notification.service.strategy.NotificationStrategy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@Service
public class NotificationServiceImpl implements INotificationService {

    private final Map<NotificationChannel, NotificationStrategy> strategies;
    private final IUserNotificationPreferenceService preferenceService;

    public NotificationServiceImpl(List<NotificationStrategy> strategyList, IUserNotificationPreferenceService preferenceService) {
        this.strategies = strategyList.stream()
                .collect(Collectors.toMap(NotificationStrategy::supports, Function.identity()));
        this.preferenceService = preferenceService;
    }

    @Async
    @Override
    public void send(NotificationRequest request) {
        try {
            // 1. Determine which channels to use based on User Preferences and Notification Category
            Set<NotificationChannel> channels = resolveChannels(request.getUserId(), request.getCategory());
            log.info("Resolved notification channels for User {} and Category {}: {}", request.getUserId(), request.getCategory(), channels);

            if (channels.isEmpty()) {
                log.info("No notification channels configured for User {} and Category {}", request.getUserId(), request.getCategory());
                return;
            }

            // 2. Dispatch to appropriate strategies
            // Polymorphism sugar :)
            for (NotificationChannel channel : channels) {
                NotificationStrategy strategy = strategies.get(channel);
                if (strategy != null) {
                    try {
                        strategy.send(request);
                    } catch (Exception e) {
                        log.error("Failed to send notification via {} to User {}", channel, request.getUserId(), e);
                    }
                } else {
                    log.warn("No strategy found for notification channel: {}", channel);
                }
            }
        } catch (Exception e) {
            // Catch-all for async execution errors to prevent thread death without log
            log.error("Unexpected error during async notification processing for User {}", request.getUserId(), e);
        }
    }

    @Override
    public void sendVerificationNotification(VerificationNotificationRequest request) {
        NotificationChannel channel = request.getChannel();

        NotificationStrategy strategy = strategies.get(channel);
        if (strategy != null) {
            try {
                strategy.send(request.getNotificationRequest());
            } catch (Exception e) {
                log.error("Failed to send notification via {} to User {}", channel, request.getUserId(), e);
            }
        } else {
            log.warn("No strategy found for notification channel: {}", channel);
        }
    }

    private Set<NotificationChannel> resolveChannels(Long userId, NotificationCategory category) {
        if (userId == null) {
            // If no user ID is provided (e.g. system notification to admin email), default to EMAIL
            return Set.of(NotificationChannel.EMAIL);
        }
        return preferenceService.getChannelsForUserAndCategory(userId, category);
    }
}
