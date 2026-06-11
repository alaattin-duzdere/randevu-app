package RandevuApp.exceptions.client;

import RandevuApp.api.ErrorCode;
import RandevuApp.exceptions.BaseApiException;

public class InvalidFileTypeException extends BaseApiException {
    public InvalidFileTypeException(String message) {
        super(ErrorCode.ERROR_UNSUPPORTED_FILE_TYPE,message);
    }
}
