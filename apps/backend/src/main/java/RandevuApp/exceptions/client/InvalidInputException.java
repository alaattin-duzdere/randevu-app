package RandevuApp.exceptions.client;

import RandevuApp.api.ErrorCode;
import RandevuApp.exceptions.BaseApiException;

public class InvalidInputException extends BaseApiException {

    public InvalidInputException(String message) {
        super(ErrorCode.ERROR_INVALID_INPUT, message);
    }

    // Use default message
    public InvalidInputException() {
        super(ErrorCode.ERROR_INVALID_INPUT);
    }
}
