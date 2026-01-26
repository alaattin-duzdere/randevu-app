package RandevuApp.domain.verification.validator.filter;

import RandevuApp.domain.user.model.User;
import RandevuApp.domain.user.model.UserStatus;
import RandevuApp.domain.user.model.VerificationStatus;
import RandevuApp.domain.verification.model.VerificationPurpose;
import RandevuApp.domain.verification.model.VerificationRequest;
import RandevuApp.exceptions.verification.VerificationPurposeException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(1)
public class UserStatusFilter implements IVerificationFilter {

    @Value("${app.security.phone-verification-validity-days}")
    private long phoneVerificationValidityDays;

    @Value("${app.security.email-verification-validity-days}")
    private long emailVerificationValidityDays;

    @Override
    public void validate(VerificationRequest request, User user) {
        VerificationPurpose purpose = request.getPurpose();
        if (purpose == null) return;

        switch (purpose) {
            case REGISTRATION:
                if (user.getStatus() == UserStatus.ACTIVE) {
                    throw new VerificationPurposeException("User is already active. Cannot start registration verification.");
                }
                break;
            case PASSWORD_RESET:
                if (user.getStatus() != UserStatus.ACTIVE) {
                    throw new VerificationPurposeException("User is not active. Cannot start password reset.");
                }
                break;
            case EMAIL_VERIFICATION:
                if (user.getEmailVerificationStatus(emailVerificationValidityDays) == VerificationStatus.VERIFIED){
                    throw new VerificationPurposeException("User is already verified. Cannot start email verification.");
                }
                break;
            case PHONE_VERIFICATION:
                if (user.getPhoneVerificationStatus(phoneVerificationValidityDays) == VerificationStatus.VERIFIED){
                    throw  new VerificationPurposeException("User is already verified. Cannot start phone verification.");
                }
                break;
            default:
                break;
        }
    }
}
