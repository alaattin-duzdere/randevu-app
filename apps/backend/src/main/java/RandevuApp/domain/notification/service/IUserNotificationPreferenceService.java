package RandevuApp.domain.notification.service;

import RandevuApp.domain.notification.dto.UpdateNotificationPreferenceRequest;
import RandevuApp.domain.notification.dto.UserNotificationPreferenceDto;
import RandevuApp.domain.notification.model.NotificationCategory;
import RandevuApp.domain.notification.model.NotificationChannel;

import java.util.List;
import java.util.Set;

public interface IUserNotificationPreferenceService {
    List<UserNotificationPreferenceDto> getUserPreferences(Long userId);
    UserNotificationPreferenceDto updatePreference(Long userId, UpdateNotificationPreferenceRequest request);
    Set<NotificationChannel> getChannelsForUserAndCategory(Long userId, NotificationCategory category);
}
