package RandevuApp.domain.verification.validator.filter;

import RandevuApp.domain.user.model.User;
import RandevuApp.domain.verification.model.VerificationRequest;

public interface IVerificationFilter {
    void validate(VerificationRequest request, User user);
}
