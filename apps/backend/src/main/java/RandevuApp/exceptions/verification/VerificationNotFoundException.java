package RandevuApp.exceptions.verification;

import RandevuApp.api.ErrorCode;
import RandevuApp.exceptions.BaseApiException;

public class VerificationNotFoundException extends BaseApiException {
    public VerificationNotFoundException(String message) {
        super(ErrorCode.ERROR_RESOURCE_NOT_FOUND, message);
    }
}
