package RandevuApp.exceptions.auth;

import RandevuApp.api.ErrorCode;
import RandevuApp.exceptions.BaseApiException;

public class InvalidCredentialsException extends BaseApiException {
    public InvalidCredentialsException() {
        super(ErrorCode.ERROR_INVALID_CREDENTIALS);
    }

    public InvalidCredentialsException(String message) {
        super(ErrorCode.ERROR_INVALID_CREDENTIALS, message);
    }
}
