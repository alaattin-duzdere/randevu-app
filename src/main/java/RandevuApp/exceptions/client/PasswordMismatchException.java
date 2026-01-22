package RandevuApp.exceptions.client;

import RandevuApp.api.ApiStatus;
import RandevuApp.exceptions.BaseApiException;

public class PasswordMismatchException extends BaseApiException {
    public PasswordMismatchException() {
        super(ApiStatus.ERROR_PASSWORD_MISMATCH);
    }
}
