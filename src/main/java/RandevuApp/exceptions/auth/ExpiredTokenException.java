package RandevuApp.exceptions.auth;

import RandevuApp.api.ApiStatus;
import RandevuApp.exceptions.BaseApiException;

public class ExpiredTokenException extends BaseApiException {
    public ExpiredTokenException() {
        super(ApiStatus.ERROR_EXPIRED_TOKEN);
    }

    public ExpiredTokenException(String message) {
        super(ApiStatus.ERROR_EXPIRED_TOKEN, message);
    }
}
