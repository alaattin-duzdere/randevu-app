package RandevuApp.domain.notification.dto;

import RandevuApp.domain.notification.model.NotificationCategory;
import RandevuApp.domain.notification.model.NotificationChannel;
import lombok.Builder;

import java.util.Set;

@Builder
public record UserNotificationPreferenceDto(
    NotificationCategory category,
    Set<NotificationChannel> channels
) {}
