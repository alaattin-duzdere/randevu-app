package RandevuApp.exceptions.auth;

import RandevuApp.api.ErrorCode;
import RandevuApp.exceptions.BaseApiException;

public class ExpiredTokenException extends BaseApiException {
    public ExpiredTokenException() {
        super(ErrorCode.ERROR_EXPIRED_TOKEN);
    }

    public ExpiredTokenException(String message) {
        super(ErrorCode.ERROR_EXPIRED_TOKEN, message);
    }
}
