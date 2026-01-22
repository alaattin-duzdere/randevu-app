package RandevuApp.exceptions.auth;

import RandevuApp.api.ApiStatus;
import RandevuApp.exceptions.BaseApiException;

public class InvalidTokenException extends BaseApiException {
    public InvalidTokenException() {
        super(ApiStatus.ERROR_INVALID_TOKEN);
    }

    public InvalidTokenException(String message) {
        super(ApiStatus.ERROR_INVALID_TOKEN, message);
    }
}
