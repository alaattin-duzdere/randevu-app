package RandevuApp.domain.notification.mapper;

import RandevuApp.domain.notification.dto.UserNotificationPreferenceDto;
import RandevuApp.domain.notification.model.UserNotificationPreference;
import org.springframework.stereotype.Component;

@Component
public class UserNotificationPreferenceMapperManual {

    public UserNotificationPreferenceDto toDto(UserNotificationPreference preference){

        return UserNotificationPreferenceDto.builder()
                .category(preference.getCategory())
                .channels(preference.getChannels())
                .build();
    }
}
