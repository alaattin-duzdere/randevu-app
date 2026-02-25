package RandevuApp.domain.business.service.impl;

import RandevuApp.config.BusinessProperties;
import RandevuApp.domain.business.dto.BusinessSettingsResponse;
import RandevuApp.domain.business.dto.UpdateBusinessSettingsRequest;
import RandevuApp.domain.business.mapper.BusinessMapper;
import RandevuApp.domain.business.model.Business;
import RandevuApp.domain.business.model.BusinessSettings;
import RandevuApp.domain.business.service.IBusinessDomainService;
import RandevuApp.domain.business.service.IBusinessSettingsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class BusinessSettingsServiceImpl implements IBusinessSettingsService {

    private final BusinessProperties businessProperties;
    private final IBusinessDomainService businessDomainService;
    private final BusinessMapper mapper;

    public BusinessSettings createDefaultSettings() {

        BusinessProperties.Defaults defaults = businessProperties.getDefaults();

        BusinessSettings defaultSettings = new BusinessSettings();
        defaultSettings.setSlotDurationTime(defaults.getSlotDurationTime());

        return defaultSettings;
    }

    @Override
    @Transactional(readOnly = true)
    public BusinessSettingsResponse getSettingsByBusinessId(Long businessId) {
        Business business = businessDomainService.getById(businessId);

        BusinessSettings settings = business.getBusinessSettings();

        return mapper.businessSettingsToBusinessSettingsResponse(settings);
    }

    @Override
    @Transactional
    public BusinessSettingsResponse updateSettings(Long businessId, UpdateBusinessSettingsRequest request) {
        Business business = businessDomainService.getById(businessId);
        BusinessSettings settings = business.getBusinessSettings();

        if (request.getSlotDurationTime() != null) {
            settings.setSlotDurationTime(request.getSlotDurationTime());
        }

        businessDomainService.save(business);

        return mapper.businessSettingsToBusinessSettingsResponse(settings);
    }
}
