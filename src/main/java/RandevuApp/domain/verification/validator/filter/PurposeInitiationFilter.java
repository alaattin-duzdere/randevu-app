package RandevuApp.domain.verification.validator.filter;

import RandevuApp.domain.user.model.User;
import RandevuApp.domain.verification.model.VerificationPurpose;
import RandevuApp.domain.verification.model.VerificationRequest;
import RandevuApp.exceptions.verification.VerificationPurposeException;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
@Order(2)
public class PurposeInitiationFilter implements IVerificationFilter {

    private static final Set<VerificationPurpose> ALLOWED_INITIATE_PURPOSES = Set.of(
            VerificationPurpose.REGISTRATION,
            VerificationPurpose.EMAIL_VERIFICATION,
            VerificationPurpose.PASSWORD_RESET,
            VerificationPurpose.PHONE_VERIFICATION
    );

    @Override
    public void validate(VerificationRequest request, User user) {
        VerificationPurpose purpose = request.getPurpose();
        if (purpose == null) return; // Default to GENERAL in service if null

        if (!ALLOWED_INITIATE_PURPOSES.contains(purpose)) {
            throw new VerificationPurposeException("Initiating verification for purpose '" + purpose + "' is not allowed via this endpoint.");
        }
    }
}
