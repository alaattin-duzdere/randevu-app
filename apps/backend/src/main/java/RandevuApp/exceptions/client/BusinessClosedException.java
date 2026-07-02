package RandevuApp.exceptions.client;

import RandevuApp.api.ErrorCode;
import RandevuApp.exceptions.BaseApiException;

public class BusinessClosedException extends BaseApiException {

    public BusinessClosedException(String message) {
        super(ErrorCode.ERROR_CONFLICT, message);
    }

    public BusinessClosedException() {
        super(ErrorCode.ERROR_CONFLICT, "İşletme bu tarihte kapalıdır.");
    }
}
