package RandevuApp.domain.service_offering.service.param;

import RandevuApp.domain.business.model.Business;

import java.math.BigDecimal;

public record CreateServiceOfferingParams(
        String name,
        String description,
        Integer duration,
        BigDecimal price,
        Business business
){
}
