package RandevuApp.exceptions.auth;

import RandevuApp.api.ErrorCode;
import RandevuApp.exceptions.BaseApiException;

public class UnauthorizedException extends BaseApiException {

    public UnauthorizedException(String message) {
        super(ErrorCode.ERROR_UNAUTHORIZED, message);
    }

    public UnauthorizedException() {
        super(ErrorCode.ERROR_UNAUTHORIZED);
    }
}
