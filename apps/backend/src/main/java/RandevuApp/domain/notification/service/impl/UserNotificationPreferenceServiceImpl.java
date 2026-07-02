package RandevuApp.domain.notification.service.impl;

import RandevuApp.config.NotificationProperties;
import RandevuApp.domain.notification.dto.UpdateNotificationPreferenceRequest;
import RandevuApp.domain.notification.dto.UserNotificationPreferenceDto;
import RandevuApp.domain.notification.mapper.UserNotificationPreferenceMapperManual;
import RandevuApp.domain.notification.model.NotificationCategory;
import RandevuApp.domain.notification.model.NotificationChannel;
import RandevuApp.domain.notification.model.UserNotificationPreference;
import RandevuApp.domain.notification.repository.UserNotificationPreferenceRepository;
import RandevuApp.domain.notification.service.IUserNotificationPreferenceService;
import RandevuApp.domain.user.model.User;
import RandevuApp.domain.user.repository.UserRepository;
import RandevuApp.exceptions.client.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserNotificationPreferenceServiceImpl implements IUserNotificationPreferenceService {

    private final UserNotificationPreferenceRepository preferenceRepository;
    private final UserRepository userRepository;
    private final UserNotificationPreferenceMapperManual mapper;
    private final NotificationProperties notificationProperties;

    public UserNotificationPreferenceServiceImpl(UserNotificationPreferenceRepository preferenceRepository, UserRepository userRepository, UserNotificationPreferenceMapperManual mapper, NotificationProperties notificationProperties) {
        this.preferenceRepository = preferenceRepository;
        this.userRepository = userRepository;
        this.mapper = mapper;
        this.notificationProperties = notificationProperties;
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserNotificationPreferenceDto> getUserPreferences(Long userId) {
        List<UserNotificationPreference> preferences = preferenceRepository.findAllByUserId(userId);
        
        return preferences.stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public UserNotificationPreferenceDto updatePreference(Long userId, UpdateNotificationPreferenceRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));

        UserNotificationPreference preference = preferenceRepository.findByUserIdAndCategory(userId, request.category())
                .orElse(UserNotificationPreference.builder()
                        .user(user)
                        .category(request.category())
                        .build());

        preference.setChannels(request.channels());
        UserNotificationPreference savedPreference = preferenceRepository.save(preference);

        return mapper.toDto(savedPreference);
    }

    @Override
    @Transactional(readOnly = true)
    public Set<NotificationChannel> getChannelsForUserAndCategory(Long userId, NotificationCategory category) {
        Optional<UserNotificationPreference> preferenceOpt = preferenceRepository.findByUserIdAndCategory(userId, category);

        if (preferenceOpt.isPresent()) {
            return preferenceOpt.get().getChannels();
        }
        return getDefaultChannels(category);
    }

    private Set<NotificationChannel> getDefaultChannels(NotificationCategory category) {
        Set<NotificationChannel> channels = notificationProperties.getDefaults().get(category);
        
        // Fallback to EMAIL if configuration is missing for a category
        if (channels == null || channels.isEmpty()) {
            return Set.of(NotificationChannel.EMAIL);
        }
        
        return channels;
    }
}
