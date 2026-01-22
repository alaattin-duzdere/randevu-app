package RandevuApp.exceptions.verification;

import RandevuApp.api.ApiStatus;
import RandevuApp.exceptions.BaseApiException;

public class VerificationExpiredException extends BaseApiException {
    public VerificationExpiredException(String message) {
        super(ApiStatus.ERROR_VERIFICATION_EXPIRED, message);
    }
}
