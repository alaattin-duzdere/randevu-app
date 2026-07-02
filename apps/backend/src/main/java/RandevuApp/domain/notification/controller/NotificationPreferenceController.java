package RandevuApp.domain.notification.controller;

import RandevuApp.domain.notification.dto.UpdateNotificationPreferenceRequest;
import RandevuApp.domain.notification.dto.UserNotificationPreferenceDto;
import RandevuApp.domain.notification.service.IUserNotificationPreferenceService;
import RandevuApp.utils.SecurityUtils;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notification-preferences")
@RequiredArgsConstructor
public class NotificationPreferenceController {

    private final IUserNotificationPreferenceService preferenceService;

    @GetMapping
    public List<UserNotificationPreferenceDto> getMyPreferences() {
        return preferenceService.getUserPreferences(SecurityUtils.getCurrentUserId());
    }

    @PutMapping
    public UserNotificationPreferenceDto updatePreference(
            @Valid @RequestBody UpdateNotificationPreferenceRequest request) {
        
        return preferenceService.updatePreference(SecurityUtils.getCurrentUserId(), request);
    }
}
