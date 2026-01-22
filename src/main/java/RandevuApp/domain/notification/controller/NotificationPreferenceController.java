package RandevuApp.domain.notification.controller;

import RandevuApp.api.CustomResponseBody;
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
    public CustomResponseBody<List<UserNotificationPreferenceDto>> getMyPreferences() {
        List<UserNotificationPreferenceDto> preferences = preferenceService.getUserPreferences(SecurityUtils.getCurrentUserId());
        return CustomResponseBody.ok(preferences,"Settings retrieved successfully");
    }

    @PutMapping
    public CustomResponseBody<UserNotificationPreferenceDto> updatePreference(
            @Valid @RequestBody UpdateNotificationPreferenceRequest request) {
        
        UserNotificationPreferenceDto updatedPreference = preferenceService.updatePreference(SecurityUtils.getCurrentUserId(), request);
        return CustomResponseBody.ok(updatedPreference,"Setting updated successfully");
    }
}
