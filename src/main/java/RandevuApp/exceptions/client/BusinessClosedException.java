package RandevuApp.exceptions.client;

import RandevuApp.api.ApiStatus;
import RandevuApp.exceptions.BaseApiException;

public class BusinessClosedException extends BaseApiException {

    public BusinessClosedException(String message) {
        super(ApiStatus.ERROR_CONFLICT, message);
    }

    public BusinessClosedException() {
        super(ApiStatus.ERROR_CONFLICT, "İşletme bu tarihte kapalıdır.");
    }
}
