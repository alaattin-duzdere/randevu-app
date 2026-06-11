package RandevuApp.exceptions.client;

import RandevuApp.api.ErrorCode;
import RandevuApp.exceptions.BaseApiException;

public class PasswordMismatchException extends BaseApiException {
    public PasswordMismatchException() {
        super(ErrorCode.ERROR_PASSWORD_MISMATCH);
    }

    public PasswordMismatchException(String message) {
        super(ErrorCode.ERROR_PASSWORD_MISMATCH, message);
    }
}
