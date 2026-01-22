package RandevuApp.exceptions.verification;

import RandevuApp.api.ApiStatus;
import RandevuApp.exceptions.BaseApiException;

public class VerificationFailedException extends BaseApiException {
    public VerificationFailedException(String message) {
        super(ApiStatus.ERROR_VERIFICATION_FAILED, message);
    }
    
    public VerificationFailedException(ApiStatus status, String message) {
        super(status, message);
    }
}
