package RandevuApp.exceptions.client;

import RandevuApp.api.ErrorCode;
import RandevuApp.exceptions.BaseApiException;

public class SlotUnavailableException extends BaseApiException {

    public SlotUnavailableException(String message) {
        super(ErrorCode.ERROR_CONFLICT, message);
    }
}
