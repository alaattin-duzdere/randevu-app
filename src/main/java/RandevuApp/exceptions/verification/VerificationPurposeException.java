package RandevuApp.exceptions.verification;

import RandevuApp.api.ApiStatus;
import RandevuApp.exceptions.BaseApiException;

public class VerificationPurposeException extends BaseApiException {
    public VerificationPurposeException(String message) {
        super(ApiStatus.ERROR_METHOD_NOT_ALLOWED, message);
    }
}
