package RandevuApp.exceptions.client;

import RandevuApp.api.ApiStatus;
import RandevuApp.exceptions.BaseApiException;

public class OwnerMismatchException extends BaseApiException {
    public OwnerMismatchException(String message) {
        super(ApiStatus.ERROR_FORBIDDEN, message);
    }
}
