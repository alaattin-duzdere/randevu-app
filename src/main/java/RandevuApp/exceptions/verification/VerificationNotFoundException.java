package RandevuApp.exceptions.verification;

import RandevuApp.api.ApiStatus;
import RandevuApp.exceptions.BaseApiException;

public class VerificationNotFoundException extends BaseApiException {
    public VerificationNotFoundException(String message) {
        super(ApiStatus.ERROR_RESOURCE_NOT_FOUND, message);
    }
}
