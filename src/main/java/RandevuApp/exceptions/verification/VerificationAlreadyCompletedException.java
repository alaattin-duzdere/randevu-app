package RandevuApp.exceptions.verification;

import RandevuApp.api.ApiStatus;
import RandevuApp.exceptions.BaseApiException;

public class VerificationAlreadyCompletedException extends BaseApiException {
    public VerificationAlreadyCompletedException(String message) {
        super(ApiStatus.ERROR_CONFLICT, message);
    }
}
