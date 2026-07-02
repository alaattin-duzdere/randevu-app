package RandevuApp.exceptions.client;

import RandevuApp.api.ErrorCode;
import RandevuApp.exceptions.BaseApiException;

public class ConflictException extends BaseApiException {

    public ConflictException(String message) {
        super(ErrorCode.ERROR_CONFLICT, message);
    }

    public ConflictException(String resourceName, String field, Object value) {
        super(ErrorCode.ERROR_CONFLICT, String.format("%s with %s '%s' already exists.", resourceName, field, value));
    }
}
