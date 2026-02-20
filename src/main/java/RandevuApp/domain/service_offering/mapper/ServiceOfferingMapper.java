package RandevuApp.domain.service_offering.mapper;

import RandevuApp.domain.service_offering.dto.ServiceOfferingResponse;
import RandevuApp.domain.service_offering.model.ServiceOffering;
import org.springframework.stereotype.Component;

@Component
public class ServiceOfferingMapper {

    public ServiceOfferingResponse entityToResponse(ServiceOffering serviceOffering) {
        return ServiceOfferingResponse.builder()
                .id(serviceOffering.getId())
                .businessId(serviceOffering.getBusiness().getId())
                .name(serviceOffering.getName())
                .description(serviceOffering.getDescription())
                .durationInMinutes(serviceOffering.getDurationInMinutes())
                .price(serviceOffering.getPrice())
                .active(serviceOffering.isActive())
                .createdAt(serviceOffering.getCreatedAt())
                .build();
    }
}
