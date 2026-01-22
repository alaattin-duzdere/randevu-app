package RandevuApp.exceptions.auth;

import RandevuApp.api.ApiStatus;
import RandevuApp.exceptions.BaseApiException;

public class ForbiddenException extends BaseApiException {

    public ForbiddenException(String message) {
        super(ApiStatus.ERROR_FORBIDDEN, message);
    }

    public ForbiddenException() {
        super(ApiStatus.ERROR_FORBIDDEN);
    }
}
