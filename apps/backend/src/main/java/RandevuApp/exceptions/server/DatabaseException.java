package RandevuApp.exceptions.server;

import RandevuApp.api.ErrorCode;
import RandevuApp.exceptions.BaseApiException;

public class DatabaseException extends BaseApiException {

    public DatabaseException(String message) {
        super(ErrorCode.ERROR_DATABASE_ERROR, message);
    }
}
