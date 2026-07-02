package RandevuApp.exceptions.client;

import RandevuApp.api.ErrorCode;
import RandevuApp.exceptions.BaseApiException;

public class ObjectDeletionException extends BaseApiException {
    public ObjectDeletionException(String message) {
        super(ErrorCode.ERROR_CONFLICT, message);
    }
}
