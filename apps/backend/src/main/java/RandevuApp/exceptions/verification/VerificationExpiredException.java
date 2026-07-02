package RandevuApp.exceptions.verification;

import RandevuApp.api.ErrorCode;
import RandevuApp.exceptions.BaseApiException;

public class VerificationExpiredException extends BaseApiException {
    public VerificationExpiredException(String message) {
        super(ErrorCode.ERROR_VERIFICATION_EXPIRED, message);
    }
}
