package RandevuApp.domain.business.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Component("businessAccess")
@RequiredArgsConstructor
public class BusinessAccessChecker {

    private final IBusinessDomainService businessDomainService;

    public boolean isOwner(Long businessId, Authentication authentication) {
        Long userId = Long.parseLong(authentication.getName());
        return businessDomainService.existsByIdAndOwnerId(businessId, userId);
    }
}
