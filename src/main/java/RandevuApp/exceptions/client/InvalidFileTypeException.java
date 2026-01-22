package RandevuApp.exceptions.client;

import RandevuApp.api.ApiStatus;
import RandevuApp.exceptions.BaseApiException;

public class InvalidFileTypeException extends BaseApiException {
    public InvalidFileTypeException(String message) {
        super(ApiStatus.ERROR_UNSUPPORTED_FILE_TYPE,message);
    }
}
