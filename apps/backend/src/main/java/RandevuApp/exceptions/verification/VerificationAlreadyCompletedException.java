package RandevuApp.exceptions.verification;

import RandevuApp.api.ErrorCode;
import RandevuApp.exceptions.BaseApiException;

public class VerificationAlreadyCompletedException extends BaseApiException {
    public VerificationAlreadyCompletedException(String message) {
        super(ErrorCode.ERROR_CONFLICT, message);
    }
}
