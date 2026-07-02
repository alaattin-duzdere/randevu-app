package RandevuApp.exceptions.verification;

import RandevuApp.api.ErrorCode;
import RandevuApp.exceptions.BaseApiException;

public class VerificationFailedException extends BaseApiException {
    public VerificationFailedException(String message) {
        super(ErrorCode.ERROR_VERIFICATION_FAILED, message);
    }
    
    public VerificationFailedException(ErrorCode status, String message) {
        super(status, message);
    }
}
