package RandevuApp.exceptions.auth;

import RandevuApp.api.ErrorCode;
import RandevuApp.exceptions.BaseApiException;

public class UserNotVerifiedException extends BaseApiException {
    public UserNotVerifiedException(String email) {
        super(ErrorCode.ERROR_USER_NOT_VERIFIED,
                String.format("User with email '%s' has not verified their account.", email));
    }
}