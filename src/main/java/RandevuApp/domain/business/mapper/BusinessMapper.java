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
        BusinessResponse response = new BusinessResponse();

        BeanUtils.copyProperties(business, response);

        response.setOwner(userMapper.userToUserResponse(business.getOwner()));
        response.setStaffList(business.getStaffList());
        response.setServiceList(business.getServiceList());
        response.setAddress(business.getAddress());

        business.getBusinessSettings().setBusiness(business); // null prevention
        BusinessSettingsResponse businessSettingsResponse = businessSettingsToBusinessSettingsResponse(business.getBusinessSettings());

        response.setBusinessSettings(businessSettingsResponse);
        return response;
    }


    public BusinessSettingsResponse businessSettingsToBusinessSettingsResponse(BusinessSettings businessSettings) {
        BusinessSettingsResponse response = new BusinessSettingsResponse();
        BeanUtils.copyProperties(businessSettings, response);

        response.setBusinessId(businessSettings.getBusiness().getId());
        return response;
    }

    public Address geoLocationResultToAddress(GeoLocationResult geoLocationResult) {
        return Address.builder()
                .city(geoLocationResult.getCity())
                .district(geoLocationResult.getDistrict())
                .fullAddress(geoLocationResult.getFormattedAddress())
                .latitude(geoLocationResult.getLatitude())
                .longitude(geoLocationResult.getLongitude())
                .provider(geoLocationResult.getProvider())
                .externalLocationId(geoLocationResult.getExternalLocationId())
                .build();
    }
}
