package RandevuApp.exceptions.auth;

import RandevuApp.api.ApiStatus;
import RandevuApp.exceptions.BaseApiException;

public class InvalidCredentialsException extends BaseApiException {
    public InvalidCredentialsException() {
        super(ApiStatus.ERROR_INVALID_CREDENTIALS);
    }
}
