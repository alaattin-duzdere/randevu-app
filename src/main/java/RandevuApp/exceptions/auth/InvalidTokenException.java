package RandevuApp.exceptions.auth;

import RandevuApp.api.ErrorCode;
import RandevuApp.exceptions.BaseApiException;

public class InvalidTokenException extends BaseApiException {
    public InvalidTokenException() {
        super(ErrorCode.ERROR_INVALID_TOKEN);
    }

    public InvalidTokenException(String message) {
        super(ErrorCode.ERROR_INVALID_TOKEN, message);
    }
}
