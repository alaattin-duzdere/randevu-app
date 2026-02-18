package RandevuApp.domain.business.controller;

import RandevuApp.api.CustomResponseBody;
import RandevuApp.domain.business.dto.BusinessSettingsResponse;
import RandevuApp.domain.business.dto.UpdateBusinessSettingsRequest;
import RandevuApp.domain.business.service.IBusinessSettingsService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/business/{businessId}/settings")
@RequiredArgsConstructor
public class BusinessSettingsController {

    private final IBusinessSettingsService businessSettingsService;

    @GetMapping
    public ResponseEntity<CustomResponseBody<BusinessSettingsResponse>> getSettings(@PathVariable Long businessId) {
        BusinessSettingsResponse settings = businessSettingsService.getSettingsByBusinessId(businessId);
        return ResponseEntity.ok(CustomResponseBody.ok(settings, "Business settings retrieved successfully"));
    }

    @PutMapping
    public ResponseEntity<CustomResponseBody<BusinessSettingsResponse>> updateSettings(
            @PathVariable Long businessId,
            @Valid @RequestBody UpdateBusinessSettingsRequest request) {
        BusinessSettingsResponse updatedSettings = businessSettingsService.updateSettings(businessId, request);
        return ResponseEntity.ok(CustomResponseBody.ok(updatedSettings, "Business settings updated successfully"));
    }
}
