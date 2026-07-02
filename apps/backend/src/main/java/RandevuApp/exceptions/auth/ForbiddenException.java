package RandevuApp.exceptions.auth;

import RandevuApp.api.ErrorCode;
import RandevuApp.exceptions.BaseApiException;

public class ForbiddenException extends BaseApiException {

    public ForbiddenException(String message) {
        super(ErrorCode.ERROR_FORBIDDEN, message);
    }

    public ForbiddenException() {
        super(ErrorCode.ERROR_FORBIDDEN);
    }
}
