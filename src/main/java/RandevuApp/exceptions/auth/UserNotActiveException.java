package RandevuApp.exceptions.auth;

import RandevuApp.api.ErrorCode;
import RandevuApp.exceptions.BaseApiException;

public class UserNotActiveException extends BaseApiException {
    public UserNotActiveException(String message) {
        super(ErrorCode.ERROR_FORBIDDEN, message);
    }
}
