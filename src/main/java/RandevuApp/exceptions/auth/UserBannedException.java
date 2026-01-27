package RandevuApp.exceptions.auth;

import RandevuApp.api.ApiStatus;
import RandevuApp.exceptions.BaseApiException;

public class UserBannedException extends BaseApiException {
    public UserBannedException(String message) {
        super(ApiStatus.ERROR_FORBIDDEN, message);
    }
}
