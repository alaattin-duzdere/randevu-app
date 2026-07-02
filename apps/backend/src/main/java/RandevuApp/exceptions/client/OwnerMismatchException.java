package RandevuApp.exceptions.client;

import RandevuApp.api.ErrorCode;
import RandevuApp.exceptions.BaseApiException;

public class OwnerMismatchException extends BaseApiException {
    public OwnerMismatchException(String message) {
        super(ErrorCode.ERROR_FORBIDDEN, message);
    }
}
