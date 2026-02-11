package RandevuApp.exceptions.client;

import RandevuApp.api.ApiStatus;
import RandevuApp.exceptions.BaseApiException;

public class ObjectDeletionException extends BaseApiException {
    public ObjectDeletionException(String message) {
        super(ApiStatus.ERROR_CONFLICT, message);
    }
}
