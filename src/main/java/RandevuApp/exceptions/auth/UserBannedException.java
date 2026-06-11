package RandevuApp.exceptions.auth;

import RandevuApp.api.ErrorCode;
import RandevuApp.exceptions.BaseApiException;

public class UserBannedException extends BaseApiException {
    public UserBannedException(String message) {
        super(ErrorCode.ERROR_FORBIDDEN, message);
    }
}
