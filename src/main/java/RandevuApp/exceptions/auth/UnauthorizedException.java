package RandevuApp.exceptions.auth;

import RandevuApp.api.ApiStatus;
import RandevuApp.exceptions.BaseApiException;

public class UnauthorizedException extends BaseApiException {

    public UnauthorizedException(String message) {
        super(ApiStatus.ERROR_UNAUTHORIZED, message);
    }

    public UnauthorizedException() {
        super(ApiStatus.ERROR_UNAUTHORIZED);
    }
}
