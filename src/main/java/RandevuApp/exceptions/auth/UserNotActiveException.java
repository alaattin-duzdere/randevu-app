package RandevuApp.exceptions.auth;

import RandevuApp.api.ApiStatus;
import RandevuApp.exceptions.BaseApiException;

public class UserNotActiveException extends BaseApiException {
    public UserNotActiveException(String message) {
        super(ApiStatus.ERROR_FORBIDDEN, message);
    }
}
