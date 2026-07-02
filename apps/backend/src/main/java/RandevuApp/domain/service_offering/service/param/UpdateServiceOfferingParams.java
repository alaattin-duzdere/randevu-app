package RandevuApp.domain.service_offering.service.param;

import RandevuApp.exceptions.client.InvalidInputException;

import java.math.BigDecimal;

public record UpdateServiceOfferingParams(
        String name,
        String description,
        Integer durationInMinutes,
        BigDecimal price,
        Boolean active
) {
    public UpdateServiceOfferingParams {
        if (durationInMinutes != null && durationInMinutes < 5) {
            throw new InvalidInputException("Hizmet süresi en az 5 dakika olmalıdır.");
        }
        if (price != null && price.compareTo(BigDecimal.ZERO) < 0) {
            throw new InvalidInputException("Fiyat 0'dan küçük olamaz.");
        }
    }
}
