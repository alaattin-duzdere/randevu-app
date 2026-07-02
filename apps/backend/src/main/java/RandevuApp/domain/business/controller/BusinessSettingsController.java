package RandevuApp.domain.business.controller;

import RandevuApp.domain.business.dto.BusinessSettingsResponse;
import RandevuApp.domain.business.dto.UpdateBusinessSettingsRequest;
import RandevuApp.domain.business.service.IBusinessSettingsService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/business/{businessId}/settings")
@RequiredArgsConstructor
public class BusinessSettingsController {

    private final IBusinessSettingsService businessSettingsService;

    @GetMapping
    public BusinessSettingsResponse getSettings(@PathVariable Long businessId) {
        return businessSettingsService.getSettingsByBusinessId(businessId);
    }

    @PutMapping
    public BusinessSettingsResponse updateSettings(
            @PathVariable Long businessId,
            @Valid @RequestBody UpdateBusinessSettingsRequest request) {
        return businessSettingsService.updateSettings(businessId, request);
    }
}
