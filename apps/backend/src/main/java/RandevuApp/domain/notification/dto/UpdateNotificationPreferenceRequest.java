package RandevuApp.domain.notification.dto;

import RandevuApp.domain.notification.model.NotificationCategory;
import RandevuApp.domain.notification.model.NotificationChannel;
import jakarta.validation.constraints.NotNull;

import java.util.Set;

public record UpdateNotificationPreferenceRequest(
    @NotNull NotificationCategory category,
    @NotNull Set<NotificationChannel> channels
) {}
