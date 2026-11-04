package RandevuApp.domain.business.service;

import RandevuApp.utils.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component("businessAccess")
@RequiredArgsConstructor
public class BusinessAccessChecker {

    private final IBusinessDomainService businessDomainService;

    public boolean isOwner(Long businessId) {
        Long userId = SecurityUtils.getCurrentUserId();
        return businessDomainService.existsByIdAndOwnerId(businessId, userId);
    }
}
