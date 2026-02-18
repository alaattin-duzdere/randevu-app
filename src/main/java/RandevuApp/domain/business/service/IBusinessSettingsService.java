package RandevuApp.domain.business.service;

import RandevuApp.domain.business.dto.BusinessSettingsResponse;
import RandevuApp.domain.business.dto.UpdateBusinessSettingsRequest;
import RandevuApp.domain.business.model.BusinessSettings;

public interface IBusinessSettingsService {

    BusinessSettings createDefaultSettings();

    BusinessSettingsResponse getSettingsByBusinessId(Long businessId);

    BusinessSettingsResponse updateSettings(Long businessId, UpdateBusinessSettingsRequest request);
}
