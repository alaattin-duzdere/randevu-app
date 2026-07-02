package RandevuApp.domain.verification.validator;

import RandevuApp.domain.user.model.User;
import RandevuApp.domain.verification.model.VerificationRequest;
import RandevuApp.domain.verification.validator.filter.IVerificationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class VerificationFilterChainManager {

    private final List<IVerificationFilter> filters;

    public void validate(VerificationRequest request, User user) {
        for (IVerificationFilter filter : filters) {
            filter.validate(request, user);
        }
    }
}
