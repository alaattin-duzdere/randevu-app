package RandevuApp.exceptions.client;

import RandevuApp.api.ApiStatus;
import RandevuApp.exceptions.BaseApiException;

public class SlotUnavailableException extends BaseApiException {

    public SlotUnavailableException(String message) {
        super(ApiStatus.ERROR_CONFLICT, message);
    }
}
