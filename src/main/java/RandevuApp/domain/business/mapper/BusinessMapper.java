package RandevuApp.domain.business.mapper;

import RandevuApp.domain.business.dto.BusinessResponse;
import RandevuApp.domain.business.dto.BusinessSettingsResponse;
import RandevuApp.domain.business.dto.GeoLocationResult;
import RandevuApp.domain.business.model.Address;
import RandevuApp.domain.business.model.Business;
import RandevuApp.domain.business.model.BusinessSettings;
import RandevuApp.domain.user.mapper.UserMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class BusinessMapper {

    private final UserMapper userMapper;

    public BusinessMapper(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    public BusinessResponse businessToBusinessResponse(Business business) {
        // Build immutable BusinessResponse record from Business entity
        BusinessSettings businessSettings = business.getBusinessSettings();
        if (businessSettings != null) businessSettings.setBusiness(business); // null prevention (preserve previous behavior)

        BusinessSettingsResponse businessSettingsResponse = businessSettings == null ? null
                : businessSettingsToBusinessSettingsResponse(businessSettings);

        return new BusinessResponse(
                business.getId(),
                business.getName(),
                business.getAddress(),
                business.getSlug(),
                business.getTimeZone(),
                business.getDescription(),
                business.isActive(),
                userMapper.userToUserResponse(business.getOwner()),
                businessSettingsResponse,
                business.getStaffList(),
                business.getServiceList()
        );
    }


    public BusinessSettingsResponse businessSettingsToBusinessSettingsResponse(BusinessSettings businessSettings) {
        // BusinessSettings model currently only holds slotDurationTime and business relation.
        Long businessId = businessSettings.getBusiness() != null ? businessSettings.getBusiness().getId() : null;
        return new BusinessSettingsResponse(
                businessSettings.getSlotDurationTime(),
                null,
                null,
                businessId
        );
    }

    public Address geoLocationResultToAddress(GeoLocationResult geoLocationResult) {
        return Address.builder()
                .city(geoLocationResult.city())
                .district(geoLocationResult.district())
                .fullAddress(geoLocationResult.formattedAddress())
                .latitude(geoLocationResult.latitude())
                .longitude(geoLocationResult.longitude())
                .provider(geoLocationResult.provider())
                .externalLocationId(geoLocationResult.externalLocationId())
                .build();
    }
}
