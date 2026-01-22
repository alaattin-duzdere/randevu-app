package RandevuApp.exceptions.server;

import RandevuApp.api.ApiStatus;
import RandevuApp.exceptions.BaseApiException;

public class DatabaseException extends BaseApiException {

    public DatabaseException(String message) {
        super(ApiStatus.ERROR_DATABASE_ERROR, message);
    }
}
