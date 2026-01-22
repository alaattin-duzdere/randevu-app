package RandevuApp.exceptions.client;

import RandevuApp.api.ApiStatus;
import RandevuApp.exceptions.BaseApiException;

public class InvalidInputException extends BaseApiException {

    public InvalidInputException(String message) {
        super(ApiStatus.ERROR_INVALID_INPUT, message);
    }

    // Use default message
    public InvalidInputException() {
        super(ApiStatus.ERROR_INVALID_INPUT);
    }
}
