package RandevuApp.exceptions.verification;

import RandevuApp.api.ErrorCode;
import RandevuApp.exceptions.BaseApiException;

public class VerificationPurposeException extends BaseApiException {
    public VerificationPurposeException(String message) {
        super(ErrorCode.ERROR_METHOD_NOT_ALLOWED, message);
    }
}
